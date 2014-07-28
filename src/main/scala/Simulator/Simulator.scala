package simulator

import org.joda.time.DateTime
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

	def simulate(date: DateTime) : org.apache.spark.rdd.RDD[CDR]

}

class BasicSimulator(
	val cellsGenerator: CellsGenerator,
	val operatorsGenerator: OperatorsGenerator,
	val usersGenerator: UsersGenerator,
	val socialNetworkGenerator: SocialNetworkGenerator
){

	def simulate(date: DateTime) : org.apache.spark.rdd.RDD[CDR] = {
		val operators = operatorsGenerator.generate()
		val cells = cellsGenerator.generate(operators)
		val users = usersGenerator.generate(cells, operators)
		val socialNetwork = socialNetworkGenerator.generate(users).cache

		socialNetwork.outDegrees.foreach{
			case (vId, d) => println(s"$vId out degree : $d")
		}

		socialNetwork.edges.flatMap{ 
			case Edge(a, b, Relation(userA, userB))=>
			Array(new CDR(
				userA,
				userB,
				userA.where(date),
				userB.where(date),
				date,
				new DateTime(0, 1, 26, 12, 0, 0, 0),
				SMS),
			new CDR(
				userA,
				userB,
				userA.where(date),
				userB.where(date),
				date,
				new DateTime(0, 1, 26, 12, 0, 0, 0),
				Call)
			)
		}
	}

}


