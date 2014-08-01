package simulator

import com.github.nscala_time.time.Imports._

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
)extends Serializable{
	protected val rand = new Random

	def simulate(day: DateTime) : org.apache.spark.rdd.RDD[CDR] = {
		val operators = operatorsGenerator.generate()
		val cells = cellsGenerator.generate(operators)
		val users = usersGenerator.generate(cells, operators)
		val socialNetwork = socialNetworkGenerator.generate(users)

		val cdrs = socialNetwork.edges.flatMap{ 
			case Edge(a, b, Relation(userA, userB))=>
				val numberOfCDR = rand.nextInt(10)
				(0 to numberOfCDR).map(_ =>
					randomCDR(userA, userB, day)
				)
		}
		val nCdr = cdrs.count

		val central = new DumUser( -1, operators(0), cells(0) )
		val centralCdrs = users.sample(false, rand.nextDouble).flatMap{ u =>
				val nOfCentralCDR = rand.nextInt(3)
				(0 to nOfCentralCDR).map(_ => 
						randomCentralCDR(u, central, day)
						)
		}
		cdrs ++ centralCdrs
	}

	def randomCDR(userA: User, userB: User, day: DateTime): CDR = {
		val date = day.hour(rand.nextInt(11)+1).
		withSecondOfMinute(rand.nextInt(59)+1)
		val cdrType = if(rand.nextDouble < 0.5) SMS else Call
		val duration = if(cdrType == SMS) 0 else rand.nextInt(1000)
		val cellA = userA.where(date)
		val cellB = userA.where(date)
		val terminationStatusA = if(cellA.drop) RingOff else Drop
		val terminationStatusB = if(cellB.drop) RingOff else Drop
		new CDR(
			userA,
			userB,
			cellA,
			cellB,
			date,
			duration,
			cdrType,
			terminationStatusA,
			terminationStatusB,
			userA.callerCost(userB, date, duration, cdrType),
			userB.callerCost(userA, date, duration, cdrType),
			TransitType.randomTransitType,
			userA.tac(date),
			userB.tac(date)
			
		)
	}

	def randomCentralCDR(user: User, central: User, day: DateTime): CDR = {
		val date = day.hour(rand.nextInt(11)+1).
		withSecondOfMinute(rand.nextInt(59)+1)
		val cellA = central.where(date)
		val cellB = user.where(date)
		val terminationStatusA = if(cellA.drop) RingOff else Drop
		val terminationStatusB = if(cellB.drop) RingOff else Drop
		new CDR(
			central,
			user,
			cellA,
			cellB,
			date,
			0,
			SMS,
			terminationStatusA,
			terminationStatusB,
			0,
			0,
			CallCenter,
			central.tac(date),
			user.tac(date)
			
		)
	}
}



