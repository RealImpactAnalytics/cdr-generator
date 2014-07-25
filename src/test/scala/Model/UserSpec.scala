import org.scalatest.FlatSpec
import org.joda.time.DateTime
import model._

class UserSpec extends FlatSpec {
	def fixture = 
		new {
			val dumUser =	new DumUser(
				1,
			// Got a problem with the Operator class (says abstract class but it
			// isn't)
				DefaultOp, //new Operator(2, "operator 2", 0.6f),
				new Cell(3, new Location(0.4, 0.6), Array())
					)
		}

	"A dum user " should "always be at the same cell" in {
		val f = fixture
		val now = new DateTime()
		1 to 23 foreach(h =>
			assert( f.dumUser.where(now) == f.dumUser.where(now.withHourOfDay(h)) )
		)
	}
	it should "always be able to call anyone, anywhere, for any duration at any time" in {
		val f = fixture
		assert( f.dumUser.isPossibleToCall(f.dumUser, new DateTime(), new DateTime()) )
	}
	it should "always be able to text anyone, anywhere, for any duration at any time" in {
		val f = fixture
		assert( f.dumUser.isPossibleToSMS(f.dumUser, new DateTime()) )
	}

	it should "always have a probability to call of 1.0" in {
		val f = fixture
		assert( f.dumUser.probabilityToCall(new DateTime()) == 1.0 ) 
	}
	it should "always have a probability to SMS of 1.0" in {
		val f = fixture
		assert( f.dumUser.probabilityToSMS(new DateTime()) == 1.0 )
	}
}
