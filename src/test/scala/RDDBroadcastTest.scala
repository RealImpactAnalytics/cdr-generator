import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import Spark.sc

class BroadCastTest(val rdd: org.apache.spark.rdd.RDD[String])

object Test{
	def main(args: Array[String]){
		val data = Array("Test1", "Te2", "Tt", "Tes", "Test")
		val bTest = new BroadCastTest(sc.parallelize(data))
		val broadCastVar = sc.broadcast(bTest)
		// Test broadCast
		broadCastVar.value.rdd.foreach(println(_))

		val data2 = Array(1,3,10,100)

		val sData = sc.parallelize(data2)
		val rddOfRdd = sData.map(x => broadCastVar.value.rdd.map(_+x.toString))
		rddOfRdd.take(3).foreach(x => println(x.first))

		sData.map(x => broadCastVar.value.rdd.map(_+x.toString).first).take(3).foreach(println(_))
	}
}
