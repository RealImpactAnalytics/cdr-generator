package generator.socialnetwork

import java.util.Random
import com.github.nscala_time.time.Imports._

import org.apache.spark._
import org.apache.spark.graphx._
import org.apache.spark.graphx.util._
import org.apache.spark.rdd._
import org.apache.spark.SparkContext._ 
import scala.reflect._

import model._
import spark.Spark.sc

/** Can generate a social network
 *  A social network is a graph of User linked by a Relation
 */
abstract class SocialNetworkGenerator {

	/** Generate a graph with the users as node
	 * @param  users The node for the graph
	 * @return       A graph of users linked by a Relation
	 */
	def generate(users: RDD[User]): Graph[User, Relation]
	
	/** Compute a rdd of pair (id, user)
	 * @param  users The rdd of user
	 * @return       A rdd o pair (id, user)
	 */
	def idUserPair(users: RDD[User]):  RDD[(Long, User)] = users.map( u => (u.id, u))
}

/** Provide methods to create a graph of T element from a graph of consecutive Int
 * from 0 to number of element -1 and a collection of element.
 * This is done by generating a temporary id.
 * 
 */
trait IntGraphUtils {
	/* Note can't use a parameter type for the relation because of a classTag
	 * problem
	 */
	
	/** Generate a graph of elements from the graph of Int.
	 *  The int of the nodes in the graph should be between 0 and number of
	 *  element -1.
	 *  The id -> elements correspondence is random
	 * 
	 * @param  intGraph			The Int graph
	 * @return              The graph of elements and the edges contains the
	 * original value + the 2 users
	 */
	def intGraphToElementGraph(users: RDD[User],intGraph: Graph[Long,Int]): Graph[User,(User,User,Int)] = {
		val idCorrespondences: RDD[(Long, User)] = users.zipWithIndex().map{
			case (u, id) => (id, u)
		}

		// Consider the edges from A to B
		// Generate pair A id, Edges
		val keyAPairWithEdge: RDD[(Long, Edge[Int])] = intGraph.edges.map{ e => 
			e match {
				case Edge(idA, idB, relation) => (idA, e)
			}
		}
		val keyBPairWithIdAAndR: RDD[(Long,(User ,Int))] = keyAPairWithEdge.join(idCorrespondences).map{
			case (_, (Edge(_, idB, relation), userA) ) => 
					(idB, (userA, relation))
		}
		val elementsEdges: RDD[Edge[(User,User,Int)]]= keyBPairWithIdAAndR.join(idCorrespondences).map{
			case (_, ( (userA, relation), userB) ) => Edge(userA.id, userB.id, (userA, userB, relation))
		}
		val vertices: RDD[(Long, User)] = users.map(e => (e.id, e))

		Graph(vertices, elementsEdges)
	}
}

/** Generate a social graph randomly by sampling the users to generate each
 * user's neigbors
 */
class RandomSocialNetworkGenerator extends SocialNetworkGenerator with IntGraphUtils{
	
	/** Generate a graph whose vertex out degree is log normal
	 * @param  users The users whose we'll be the node
	 * @return       A graph of user
	 */
	override def generate(users: RDD[User]): Graph[User, Relation]= {
		// Need to map the vertices because logNormal give an int attribute
		val t = ClassTag[Relation]_
		val intGraph = GraphGenerators.logNormalGraph(sc, users.count.toInt).mapVertices{
			case (id, v) => v.toLong
		}

		intGraph.outDegrees.foreach{
			case (vId, d) => println(s"int graph : $vId out degree : $d")
		}

		val userGraph = intGraphToElementGraph(users, intGraph)
		userGraph.mapEdges[Relation]( (partitionId: PartitionID, edgesIterator: Iterator[Edge[(User,User,Int)]]) =>
				edgesIterator.map{
				case Edge(a, b, (userA, userB, rel)) => new BasicRelation(userA, userB)
			}
		)
	}
}
