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

	override def toString(): String = this.toString(",")
	def toString(separator: String): String ={
		val tmp = this.toMap().values.mkString(separator)
		println(tmp)
		tmp
	}

	def header(separator: String): String = this.toMap().keys.mkString(separator)

	private def toMap() = {
		Map(
			"fromUserId" -> fromUser.id.toString,
			"toUserId" -> toUser.id.toString,
			"fromOperator" -> fromUser.operator.name,
			"toOperator" -> toUser.operator.name,
			"duration" -> duration.toString("%y%m%d"),
			"date" -> date.toString("%y%m%d"),
			"type" -> CDRType.toString(cdrType))
	}
}

