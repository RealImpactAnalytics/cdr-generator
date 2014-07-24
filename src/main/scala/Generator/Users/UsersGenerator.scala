package generator.users

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import spark.Spark.sc

import model._

abstract class UsersGenerator(){
	def generate(
		cells: Array[Cell],
		operators: Array[Operator] 
		) : org.apache.spark.rdd.RDD[User]
}

class BasicUsersGenerator(nUsers: Int) extends UsersGenerator {

	override def generate(
		cells: Array[Cell],
		operators: Array[Operator] 
		) : org.apache.spark.rdd.RDD[User] = {

			sc.parallelize( 1 to nUsers map{ id => 
				new DumUser( id, operators(0), cells(0) )
			})

	}
}
