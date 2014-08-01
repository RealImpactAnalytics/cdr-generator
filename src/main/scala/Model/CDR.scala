package model
import com.github.nscala_time.time.Imports._

import java.util.Random

abstract class CDRType
object SMS extends CDRType
object Call extends CDRType

object CDRType{
	def toString(cdrType: CDRType): String ={
		cdrType match {
			case SMS => "SMS"
			case Call => "call"
			case _ => "unknown"
		}
	}
}

abstract class TerminationStatus
object Drop extends TerminationStatus
object RingOff extends TerminationStatus

object TerminationStatus{
	def toString(terminationStatus: TerminationStatus): String ={
		terminationStatus match {
			case Drop => "1"
			case RingOff => "0"
			case _ => "unknown"
		}
	}
}

abstract class TransitType
/** Same operator
 */
object OnNet extends TransitType
/** Different operator
*/
object OffNet extends TransitType
/** International call
*/
object International extends TransitType
/** From/To a Call Center 
 */
object CallCenter extends TransitType
/** Special number like 911
 */
object Special extends TransitType

object TransitType{
	protected val rand = new Random
	def toString(transitType: TransitType): String ={
		transitType match {
			case OnNet => "ONNET"
			case OffNet => "OFFNET"
			case International => "INT"
			case CallCenter => "CALLCENTER"
			case Special => "SPECIAL"
			case _ => "UNKNOWN"
		}
	}

	def randomTransitType: TransitType = {
		val values = Array( OnNet, OffNet, International, CallCenter, Special)
		values( rand.nextInt(values.size) )
	}
}

object TAC{
	protected val rand = new Random
	val tacMap = Map(
	"IPhone4" -> "01241700",
	"Nexus5" -> "35824005"
	)

	/** Return a random TAC
	 */
	def randomTac : String = {
		val values = tacMap.values.toArray
		values( rand.nextInt(tacMap.size) )
	}
}

/** Represent a CDR
 * @param fromUser          User who call/SMS
 * @param toUser            User who recieve the call/SMS
 * @param fromCell          The cell from which fromUser call/SMS
 * @param toCell            The cell from which toUser call/SMS
 * @param date              The date of the call/SMS
 * @param duration          Duration of the call/SMS
 * @param cdrType           Call/SMS
 * @param fromTerminationStatus How the call/SMS terminated (from)
 * @param toTerminationStatus How the call/SMS terminated (to)
 * @param fromValue             Cost of the caller
 * @param toValue             Cost of the reciever
 * @param transitType       OnNet/OffNet call/SMS
 * @param fromTac               Device TAC of the caller
 * @param toTac               Device TAC of the receiver
 */
class CDR(
	val fromUser: User,
	val toUser: User,
	val fromCell: Cell,
	val toCell: Cell,
	val date: DateTime,
	val duration: Int,
	val cdrType: CDRType,
	val fromTerminationStatus: TerminationStatus,
	val toTerminationStatus: TerminationStatus,
	val fromValue: Double,
	val toValue: Double,
	val transitType: TransitType,
	val fromTac: String,
	val toTac: String
) extends Serializable {

	/** CDR fields separate by a "," in the same order has the header
	 *
	 * @return  String
	 */
	override def toString(): String = this.toString(",")

	/** CDR fields separate by separator in the same order has the header
	 * @param separator		The separator
	 * @return  String		The concatenation of the cdr fields
	 */
	def toString(separator: String): String ={
		val tmp = this.toMap().values.mkString(separator)
		println(tmp)
		tmp
	}

	/** Concatenation of the cdr fields that will be return by toString
	 *
	 * @param  separator String
	 * @return           String
	 */
	def header(separator: String): String = this.toMap().keys.mkString(separator)

	private def toMap() = {
		Map(
			"MSISDN_1" -> fromUser.id.toString,
			"MSISDN_2" -> toUser.id.toString,
			"CELL_1" -> fromCell.id,
			"CELL_2" -> toCell.id,
			"OPERATOR_1" -> fromUser.operator.name,
			"OPERATOR_2" -> toUser.operator.name,
			"DURATION" -> duration.toString,
			"TIMESTAMP" -> date.toString("%y%m%d%h%s"),
			"TERMINATION_STATUS_1" -> TerminationStatus.toString(fromTerminationStatus),
			"TERMINATION_STATUS_2" -> TerminationStatus.toString(toTerminationStatus),
			"VALUE_1" -> fromValue.toString,
			"VALUE_2" -> toValue.toString,
			"TYPE" -> CDRType.toString(cdrType),
			"TRANSIT_TYPE" -> TransitType.toString(transitType),
			"TAC_1" -> fromTac,
			"TAC_2" -> toTac
		)
	}
}

/** Defaulft CDR for testing
 */
object DefaultCDR extends CDR(
	DefaultDumUser,
	DefaultDumUser,
	DefaultCell,
	DefaultCell,
	DateTime.now,
	100,
	SMS,
	RingOff,
	RingOff,
	10,
	20,
	OnNet,
	TAC.randomTac,
	TAC.randomTac
)
