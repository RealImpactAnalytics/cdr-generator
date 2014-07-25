import org.scalatest.FlatSpec
import org.joda.time.DateTime
import model._

class CDRSpec extends FlatSpec {
	def fixture = new {
		val cdr = new CDR(
			DefaultDumUser,
			DefaultDumUser,
			DefaultCell,
			DefaultCell,
			new DateTime(),
			new DateTime(),
			SMS)
	}

	"CDR.toString fields" should "be in the same order as the fields return by the header function" in {
		val cdr = fixture.cdr
		val fields = cdr.toString(",").split(",")
		val header = cdr.header(",").split(",")

		header.zip(fields).foreach{ case (h,f) =>
				h match {
					case "fromUserId" => assert( f == cdr.fromUser.id.toString )
					case "toUserId" => assert( f == cdr.toUser.id.toString )
					case "fromOperator" => assert( f == cdr.fromUser.operator.name )
					case "toOperator" => assert( f == cdr.toUser.operator.name )
					case "duration" => assert( f == cdr.duration.toString("%y%m%d") )
					case "date" => assert( f == cdr.date.toString("%y%m%d") )
					case "type" => assert( f == CDRType.toString(cdr.cdrType))
					case _ => assert(false)
				}
			}
	}
	it should "remain the same" in {
		val cdr = fixture.cdr
		val headerFields = cdr.header(",").split(",")
		val fields = Array(
			"fromUserId", 
			"toUserId",  
			"fromOperator", 
			"toOperator", 
			"duration", 
			"date", 
			"type"
			)
		fields.foreach( f => assert(headerFields.contains(f)) )
	}

}
