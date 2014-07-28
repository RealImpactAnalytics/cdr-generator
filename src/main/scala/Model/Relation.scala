package model


/** Express the relation between 2 Users
 * @param a one user of the relation
 * @param b one user of the relation
 */
abstract case class Relation (
	val a: User,
	val b: User){

	/** Force of the relation  
	 *
	 * @return  The force of the relation
	 */
	def force: Double
}

/** Express a simple relation between 2 Users which always have the same force
 * @param a one user of the relation
 * @param b one user of the relation
 */
class BasicRelation(
	a:User,
	b:User) extends Relation(a, b){
	
	def force = 1.0

}
