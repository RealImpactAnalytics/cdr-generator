package generator.cells

import model._
import scala.util.Random
import scala.util.Random._
import org.joda.time.DateTime
import org.joda.time.DateTime._

/** CellsGenerator interface */
abstract class CellsGenerator(){
	/**
	  * Generate an array of cells
		* @return an array of cells
		*/
	def generate() : Array[Cell]
}

/**
	* Generate cell located at random position (x, y)
	* Where x is in [latMin, latMax] and y is in [lonMin, lonMax]
	*
	* @param nCells int, the number of cells to generate
	* @param latMin Double, the latitude of the bottom left corner of the square, default = 0.0
	* @param latMax Double, the latitude of the top right corner of the square, default = 1.0
	* @param lonMin Double, the longitude of the bottom left corner of the square, default = 0.0
	* @param lonMax Double, the longitude of the top right corner of the square, default = 1.0
	*/
class BasicCellsGenerator(
	val nCells: Int,
	val operators: Array[Operator],
	val latMin: Double = 0,
	val latMax: Double = 1,
	val lonMin: Double = 0,
	val lonMax: Double = 1
) extends CellsGenerator {
	private val rand = new Random( (new DateTime()).getMillisOfDay() )

	override def generate() : Array[Cell] = {
		val cellsId = 1 to nCells
		cellsId.map{ id => new Cell(id, randomLocation(), Array()) }.toArray
	}

	private def randomLocation() = new Location(
		latMin + (rand.nextDouble() / (latMax - latMin)),
		lonMin + (rand.nextDouble() / (lonMax - lonMin)) )
}


