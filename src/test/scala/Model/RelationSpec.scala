import org.scalatest.FlatSpec
import model._

class RelationSpec extends FlatSpec {
	def fixture = new {
		val basicRelation = new BasicRelation(DefaultDumUser, DefaultDumUser)
	}

	"BasicRelation" should "always have a force of 1.0" in {
		val f = fixture
		assert( f.basicRelation.force == 1.0, "The force of the relation isn't 1.0")
	}
}
