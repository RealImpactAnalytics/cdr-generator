package generator.users

import model._

abstract class UsersGenerator(){
	def generate(): org.apache.spark.rdd.RDD[User]
}
