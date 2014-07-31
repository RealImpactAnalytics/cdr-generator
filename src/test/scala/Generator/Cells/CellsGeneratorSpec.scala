import org.scalatest.FlatSpec
import scala.util.Random
import scala.util.Random._
import org.joda.time.DateTime
import generator.cells._
import model._

class CellsGeneratorSpec extends FlatSpec {
	def fixture = new {
		val operators = Array(DefaultOperator.asInstanceOf[Operator])
	}

	"BasicCellsGenerator" should "generate the right number of cells" in {
		val f = fixture
		val nCells = 50
		val basicCellsGenerator = new BasicCellsGenerator(50)
		assert( basicCellsGenerator.generate(f.operators).size == nCells )
	}
	it should "generate cells in the given boundaries" in {
		val f = fixture
		val rand = new Random
		val nCells = 10
		1 to 10 foreach{_ =>
			val latMin = rand.nextDouble.abs
			val latMax = latMin + rand.nextDouble.abs
			val lonMin = rand.nextDouble.abs
			val lonMax = lonMin + rand.nextDouble.abs
			println(s"latMin: $latMin latMax: $latMax lonMin: $lonMin lonMax: $lonMax")
			val basicCellsGenerator = 
				new BasicCellsGenerator(50,latMin, latMax, lonMin, lonMax)
			val cells = basicCellsGenerator.generate(f.operators)
			cells.foreach{ c =>
					assert( c.location.lat >= latMin,
						s"lat : ${c.location.lat} below the min latitude : ${latMin}" )
					assert( c.location.lat <= latMax,
						s"lat : ${c.location.lat} over the max latitude : ${latMax}" )
					assert( c.location.lon >= lonMin,
						s"lon : ${c.location.lon} below the min longitude : ${lonMin}" )
					assert( c.location.lon <= lonMax,
						s"lon : ${c.location.lon} over the max longitude : ${lonMax}" )
					}
		}
	}

}
