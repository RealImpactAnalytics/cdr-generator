package model

import org.joda.time.DateTime

/*
 * Define an abstract user which can be made concrete with traits mixins
 */ 

abstract class User( val id: Long, var operator : Operator ) extends Serializable{
	
	/*
	 * @return true if 
	 */
	def isPossibleToCall(other : User, date: DateTime, duration: DateTime): Boolean

	def isPossibleToSMS(other : User, date: DateTime): Boolean

	def probabilityToCall(date : DateTime): Double

	def where(date: DateTime): Cell

}

class DumUser( id: Long, operator: Operator, cell: Cell ) extends User(id, operator) {
	
	override def isPossibleToCall(other : User, date: DateTime, duration: DateTime): Boolean = true

	override def isPossibleToSMS(other : User, date: DateTime): Boolean = true

	override def probabilityToCall(date : DateTime): Double = 1.0

	def where(date: DateTime): Cell = cell

}
