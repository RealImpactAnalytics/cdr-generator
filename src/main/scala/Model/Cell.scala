package model

case class Location(x: Double, y: Double)

class Cell(
	val id: Long,
	val location: Location,
	val cellsNeighbor: Array[Cell]
)

