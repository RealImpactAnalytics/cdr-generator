package model
import org.joda.time.DateTime

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

class CDR(
	val fromUser: User,
	val toUser: User,
	val fromCell: Cell,
	val toCell: Cell,
	val date: DateTime,
	val duration: DateTime,
	val cdrType: CDRType
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
			"fromUserId" -> fromUser.id.toString,
			"toUserId" -> toUser.id.toString,
			"fromCell" -> fromCell.id,
			"toCell" -> toCell.id,
			"fromOperator" -> fromUser.operator.name,
			"toOperator" -> toUser.operator.name,
			"duration" -> duration.toString("%y%m%d"),
			"date" -> date.toString("%y%m%d"),
			"type" -> CDRType.toString(cdrType))
	}
}

/** Defaulft CDR for testing
 */
object DefaultCDR extends CDR(
	DefaultDumUser,
	DefaultDumUser,
	DefaultCell,
	DefaultCell,
	new DateTime(),
	new DateTime(),
	SMS)
