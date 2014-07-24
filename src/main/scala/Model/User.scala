package model

import org.joda.time.DateTime

/*
 * Define an abstract user which can be made concrete with traits mixins
 */ 

abstract class User( val id: Long, var operator : Operator){
	
	def isPossibleToCall(other : User)

	def isPossibleToSMS(other : User)

	def probabilityToCall(date : DateTime)

}
