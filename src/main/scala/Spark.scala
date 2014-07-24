import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import java.io.File


object Spark{
		val conf = new SparkConf().setAppName("Results test")
		.setMaster("local")
		.setSparkHome("~/spark-1.0.0")
		.setJars(new File( "target/scala-2.10" ).listFiles.filter( x => x.isFile && x.getName.toLowerCase.takeRight( 4 ) == ".jar" ).toList.map( _.toString ))

		val sc = new SparkContext(conf)
}
