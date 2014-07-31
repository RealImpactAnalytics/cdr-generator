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
	}

	"CDR.toString fields" should "be in the same order as the fields return by the header function" in {
		val cdr = fixture.cdr
		val fields = cdr.toString(",").split(",")
		val header = cdr.header(",").split(",")

		header.zip(fields).foreach{ case (h,f) =>
				h match {
					case "MSISDN_1" => assert( f == cdr.fromUser.id.toString )
					case "MSISDN_2" => assert( f == cdr.toUser.id.toString )
					case "CELL_1" => assert( f == cdr.fromCell.id.toString )
					case "CELL_2" => assert( f == cdr.toCell.id.toString )
					case "OPERATOR_1" => assert( f == cdr.fromUser.operator.name )
					case "OPERATOR_2" => assert( f == cdr.toUser.operator.name )
					case "DURATION" => assert( f == cdr.duration.toString )
					case "TIMESTAMP" => assert( f == cdr.date.toString("%y%m%d%h%s") )
					case "TERMINATION_STATUS_1" => assert( f == TerminationStatus.toString(cdr.fromTerminationStatus) )
					case "TERMINATION_STATUS_2" => assert( f == TerminationStatus.toString(cdr.toTerminationStatus) )
					case "VALUE_1" => assert( f == cdr.fromValue.toString )
					case "VALUE_2" => assert( f == cdr.toValue.toString )
					case "TYPE" => assert( f == CDRType.toString(cdr.cdrType) )
					case "TRANSIT_TYPE" => assert( f == TransitType.toString(cdr.transitType) )
					case "TAC_1" => assert( f == cdr.fromTac )
					case "TAC_2" => assert( f == cdr.toTac)
					case s => assert(false, s"The field $s shouldn't be in the header")
				}
			}
	}
	it should "remain the same" in {
		val cdr = fixture.cdr
		val headerFields = cdr.header(",").split(",")
		val fields = Array(
			"MSISDN_1",
			"MSISDN_2",
			"CELL_1",
			"CELL_2",
			"OPERATOR_1",
			"OPERATOR_2",
			"DURATION",
			"TIMESTAMP",
			"TERMINATION_STATUS_1",
			"TERMINATION_STATUS_2",
			"VALUE_1",
			"VALUE_2",
			"TYPE",
			"TRANSIT_TYPE",
			"TAC_1",
			"TAC_2"
			)
		fields.foreach( f => assert(headerFields.contains(f), s"The header don't have the field $f") )
	}

}
