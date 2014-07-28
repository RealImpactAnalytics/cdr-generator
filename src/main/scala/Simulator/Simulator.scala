package simulator

import org.joda.time.DateTime
import org.joda.time.DateTime._
import java.util.Random
import org.apache.spark.graphx._

import generator.cells._
import generator.operators._
import generator.users._
import generator.socialnetwork._
import model._

abstract class Simulator(
	val cellsGenerator: CellsGenerator,
	val operatorsGenerator: OperatorsGenerator,
	val usersGenerator: UsersGenerator
){

	/** Generate one day of cdr based on the results of the other generator
	 * @param  day 
	 * @return     CDRs for the day
	 */
	def simulate(day: DateTime) : org.apache.spark.rdd.RDD[CDR]

}

class BasicSimulator(
	val cellsGenerator: CellsGenerator,
	val operatorsGenerator: OperatorsGenerator,
	val usersGenerator: UsersGenerator,
	val socialNetworkGenerator: SocialNetworkGenerator
){

	def simulate(day: DateTime) : org.apache.spark.rdd.RDD[CDR] = {
		val operators = operatorsGenerator.generate()
		val cells = cellsGenerator.generate(operators)
		val users = usersGenerator.generate(cells, operators)
		val socialNetwork = socialNetworkGenerator.generate(users)

		socialNetwork.edges.flatMap{ 
			case Edge(a, b, Relation(userA, userB))=>
				val rand = new Random(now.getMillisOfSecond)
				val date = day.withHourOfDay(rand.nextInt(11)+1).
				withSecondOfMinute(rand.nextInt(59)+1)
				val duration = rand.nextInt(1000)
				Array(new CDR(
					userA,
					userB,
					userA.where(date),
					userB.where(date),
					date,
					duration,
					SMS),
				new CDR(
					userA,
					userB,
					userA.where(date),
					userB.where(date),
					date,
					duration,
					Call)
				)
		}
	}

}


