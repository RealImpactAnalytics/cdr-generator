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

	"CDR header" should "be in a specific order" in {
		val f = fixture
		assert( f.cdr.header(",") == "fromUserId,toUserId,fromOperator,toOperator,duration,date,type" )
	}

}
