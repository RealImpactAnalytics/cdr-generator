package model

import org.joda.time.DateTime

/** Represent an User
 *
 * @param id       The User Id
 * @param operator The user Operator
 */
abstract class User( val id: Long, var operator : Operator ) extends serializable{
	
	/** Check if this user can call the other user at that date for that duration.
	 *
	 * @param  other    The other user
	 * @param  date     The time of the call
	 * @param  duration The duration of the call
	 * @return          True if the user can call else false
	 */
	def isPossibleToCall(other : User, date: DateTime, duration: DateTime): Boolean

	/** Check if this user can text the other user at that date.
	 *
	 * @param  other    The other user
	 * @param  date     The time of the SMS
	 * @return          True if the user can SMS else false
	 */
	def isPossibleToSMS(other : User, date: DateTime): Boolean

	/** Compute how probable is it for this user to call that date.
	 *
	 * @param  date		  The date of the call
	 * @return					The propability that the user call
	 */
	def probabilityToCall(date : DateTime): Double

	/** Compute how probable is it for this user to SMS that date.
	 *
	 * @param  date		  The date of the SMS
	 * @return					The propability that the user SMS
	 */
	def probabilityToSMS(date : DateTime): Double

	/** In function of the position of the user at that time, compute the cell to 
	 * which he we'll be connected.
	 *
	 * @param  date DateTime
	 * @return      Cell
	 */
	def where(date: DateTime): Cell

}

class DumUser( id: Long, operator: Operator, cell: Cell ) extends User(id, operator) {
	
	override def isPossibleToCall(other : User, date: DateTime, duration: DateTime): Boolean = true

	override def isPossibleToSMS(other : User, date: DateTime): Boolean = true

	override def probabilityToCall(date : DateTime): Double = 1.0

	override def probabilityToSMS(date : DateTime): Double = 1.0

	def where(date: DateTime): Cell = cell

}
