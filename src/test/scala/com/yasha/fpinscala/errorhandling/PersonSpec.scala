package com.yasha.fpinscala.errorhandling
//
//import com.yasha.fpinscala.UnitSpec
//import scala.{Option => _, Some => _, Either => _, Left => _, Right => _, None => _, _}
//
//class PersonSpec extends UnitSpec {
//  import Person._
//
//  "A person" should "return the appropriate 'error value' if the name or age is invlaid" in {
//    assertResult(Left("Age is out of range."))(mkPerson("Bob", -1))
//    assertResult(Left("Name is empty."))(mkPerson("", 10))
//    assertResult(Left("Name is empty."))(mkPerson(null, 10))
//  }
//  it should "return only the name 'error value' if both the name and age are invalid" in {
//    assertResult( Left("Name is empty.") )( mkPerson(null, -1) )
//  }
//
//}
