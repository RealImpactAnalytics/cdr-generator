import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import com.github.nscala_time.time.Imports._

import spark.Spark.sc
import generator.users._
import generator.operators._
import generator.cells._
import generator.socialnetwork._
import simulator._

object CDRSimulation{
	def main(args: Array[String]){
		val sim = new BasicSimulator(
			new BasicCellsGenerator(10),
			new HarcodedOperatorsGenerator(),
			new BasicUsersGenerator(50),
			new RandomSocialNetworkGenerator()
		)
		sim.simulate(new DateTime).map(_.toString).saveAsTextFile("test.txt")
	}
}
