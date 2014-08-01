package model

import com.github.nscala_time.time.Imports._
import scala.util.Random


/** Represent an User
 *
 * @param id       The User Id
 * @param operator The user Operator
 */
abstract class User( val id: Long, var operator : Operator) extends Serializable{
	
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

	/** Compute the tac number of the device the user used at that date
	 * @return  TAC		The tac number of the device used by the user
	 */
	def tac(date: DateTime): String

	/** Compute the cost for this user to call/SMS (cdrType) the user to at that
	 * date for that duration.
	 * @param  to       The user this user call/SMS
	 * @param  date     The date of the call/SMS
	 * @param  duration The duration of the call/SMS
	 * @param  CDRType	The type of the cdr => if it's a call or a SMS
	 * @return          The cost
	 */
	def callerCost(to: User, date: DateTime, duration: Int, cdrType: CDRType): Double

	/** Compute the cost for this user to recieve a call/SMS (cdrType) from the
	 * user from at that date for that duration.
	 * @param  from     The user who call/SMS this user
	 * @param  date     The date of the call/SMS
	 * @param  duration The duration of the call/SMS
	 * @param  CDRType	The type of the cdr => if it's a call or a SMS
	 * @return          The cost
	 */
	def recieverCost(from: User, date: DateTime, duration: Int, cdrType: CDRType): Double

}

class DumUser( id: Long, operator: Operator, cell: Cell ) extends User(id, operator) with Serializable {
	def this() = this(1, DefaultOperator, DefaultCell)
	
	override def isPossibleToCall(other : User, date: DateTime, duration: DateTime): Boolean = true

	override def isPossibleToSMS(other : User, date: DateTime): Boolean = true

	override def probabilityToCall(date : DateTime): Double = 1.0

	override def probabilityToSMS(date : DateTime): Double = 1.0

	override def where(date: DateTime): Cell = cell

	override def tac(date: DateTime): String = TAC.randomTac

	override def callerCost(to: User, date: DateTime, duration: Int, cdrType: CDRType): Double = {
		10 //Random.nextDouble * 10
	}

	override def recieverCost(from: User, date: DateTime, duration: Int, cdrType: CDRType): Double = {
		10 //if( Random.nextDouble < 0.9 ) Random.nextDouble else 0.0
	}
}

/** Default DumUser for testing
 */
object DefaultDumUser extends DumUser(
	1,
	DefaultOperator,
	DefaultCell)
