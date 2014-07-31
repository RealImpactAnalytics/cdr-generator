package util.graph

import math._
import java.util.Random
import org.apache.spark.graphx._

case class Node(val id: Int, val degree: Int)
case class Community(val id: Int, val degree: Int)

/** Graph generator based on the article of A. Lancichinetti, S. Fortunato
 * and F. Radicchi : Benchmark graphs for testing community detection algorithme
 * Classical values are lambda = 2, beta = 1 and mu = 0,1
 * 
 * @param N      The number of node
 * @param lambda The exponent for the power law use to determine node degrees
 * @param beta   The exponent for the power law use to determine community size
 * @param mu     The fraction of links a node have within his commmunity between
 *								0 and 1
 */
class GraphBenchmark(val N: Int, val lambda: Float, val beta: Float, val mu: Float) {
	/*
	 * Building Step
	 * 1	Assign to each node a degree
	 * 2	Assign to each community a size
	 * 3	Put each node in a community
	 *		3.1		All node are "homeless"
	 *		3.2		Assign each "homeless" node to a community
	 *		3.4		If a community is to big, kick out a random node and made hime
	 *					homeless.
	 *		3.5		Go to 3.2
	 * 4	Compute the edges 
	 *		4.1		Within a community
	 *		4.2		Between each community
	 */
//	def generate(): Graph[Int, Int] = {
	//}

	def nextPowerLawValue(rand: Random, n: Float, min: Int, max: Int){
		pow((( pow(max, n+1) - pow(min, n+1) )*rand.nextDouble + pow(min, n+1) ), 1/n+1)
	}
}
