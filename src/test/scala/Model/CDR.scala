package model
import org.joda.time.DateTime

abstract class CDRType
case class SMS extends CDRType
case class Call extends CDRType

class CDR(
	val fromUser: User,
	val toUser: User,
	val fromCell: Cell,
	val toCell: Cell,
	val date: DateTime,
	val cdrType: CDRType
)
