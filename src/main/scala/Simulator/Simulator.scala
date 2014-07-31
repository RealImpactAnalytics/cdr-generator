package simulator

import com.github.nscala_time.time.Imports._


import scala.util.Random
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
	private val rand = new Random

	def simulate(day: DateTime) : org.apache.spark.rdd.RDD[CDR] = {
		val operators = operatorsGenerator.generate()
		val cells = cellsGenerator.generate(operators)
		val users = usersGenerator.generate(cells, operators)
		val socialNetwork = socialNetworkGenerator.generate(users)

		socialNetwork.edges.flatMap{ 
			case Edge(a, b, Relation(userA, userB))=>
				val numberOfCDR = rand.nextInt(10)
				(0 to numberOfCDR).map(_ =>
					randomCDR(userA, userB, day)
				)
		}
	}

	def randomCDR(userA: User, userB: User, day: DateTime): CDR = {
		val date = day.hour(rand.nextInt(11)+1).
		withSecondOfMinute(rand.nextInt(59)+1)
		val duration = rand.nextInt(1000)
		val costA = rand.nextDouble * 10
		val costB = rand.nextDouble * 10
		val cdrType = if(rand.nextDouble < 0.5) SMS else Call
		val terminationStatusA = if(rand.nextDouble < 0.95) RingOff else Drop
		val terminationStatusB = if(rand.nextDouble < 0.95) RingOff else Drop
		new CDR(
			userA,
			userB,
			userA.where(date),
			userB.where(date),
			date,
			duration,
			cdrType,
			terminationStatusA,
			terminationStatusB,
			costA,
			costB,
			TransitType.randomTransitType,
			TAC.randomTac,
			TAC.randomTac
		)
	}

}


