package model

case class Location(x: Double, y: Double)

class Cell (
	val id: Long,
	val location: Location,
	val operator: Operator,
	val cellsNeighbor: Array[Cell]
)extends Serializable


/** Default Cell for testing
 */
object DefaultCell extends Cell(
	1,
	new Location(0, 0),
	DefaultOperator,
	Array()
)

