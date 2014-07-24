package Simulator

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


