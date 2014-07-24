package simulator

import org.joda.time.DateTime

import generator.cells._
import generator.operators._
import generator.users._
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
	val usersGenerator: UsersGenerator
){

	def simulate(date: DateTime) : org.apache.spark.rdd.RDD[CDR] = {
		val operators = operatorsGenerator.generate()
		val cells = cellsGenerator.generate(operators)
		val users = usersGenerator.generate(cells, operators)
		users.flatMap{ u =>
			Array(new CDR(
				u,
				u,
				u.where(date),
				u.where(date),
				date,
				new DateTime(0, 1, 26, 12, 0, 0, 0),
				SMS),
			new CDR(
				u,
				u,
				u.where(date),
				u.where(date),
				date,
				new DateTime(0, 1, 26, 12, 0, 0, 0),
				Call)
			)
		}
	}

}


