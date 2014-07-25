package model

class Operator (
	val id: Long,
	val name: String,
	val share: Float
	) extends Serializable

object DefaultOp extends Operator(1, "first", 0.1f)

