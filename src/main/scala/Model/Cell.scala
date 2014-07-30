package model

import scala.util.Random
import scala.util.Random._
import org.joda.time.DateTime
import org.joda.time.DateTime._

case class Location(lat: Double, lon: Double)

/**
 * @param dropProbability The probability that the cell have to drop a call
 */
class Cell (
	val id: Long,
	val location: Location,
	val operator: Operator,
	val cellsNeighbor: Array[Cell],
	val dropProbability: Double = 0.01
)extends Serializable {
	private val rand = new Random( (new DateTime()).getMillisOfDay() )

	/** Compute if the cell willl drop the next call or not
	 * @return  true if the cell drop the call false otherwise
	 */
	def drop : Boolean = rand.nextDouble <= dropProbability 
}


/** Default Cell for testing
 */
object DefaultCell extends Cell(
	1,
	new Location(0, 0),
	DefaultOperator,
	Array()
)

