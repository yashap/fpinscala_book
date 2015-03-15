package com.yasha.fpinscala.datastructures

import com.yasha.fpinscala.UnitSpec

class ListSpec extends UnitSpec {
  import List._

  def listFixture =
    new {
      val as: List[Int] = Cons(10, Cons(100, Cons(5, Cons(50, Cons(2, Cons(20, Nil))))))
      val bs: List[Double] = Cons(10.1, Cons(100.1, Cons(5.5, Cons(50.5, Cons(2.2, Cons(20.2, Nil))))))
    }

  "A List" should "have a head equal to its first element" in {
    // if I want the test to only run in certain cases, I can add this at the start
    // assume(booleanCondition)
    val f = listFixture

    assertResult(10)(head(f.as))
    assertResult(10.1)(head(f.bs))
  }

  it should "have a tail equal to its last elements" in {
    val f = listFixture
    val ast: List[Int] = Cons(100, Cons(5, Cons(50, Cons(2, Cons(20, Nil)))))
    val bst: List[Double] = Cons(100.1, Cons(5.5, Cons(50.5, Cons(2.2, Cons(20.2, Nil)))))

    assertResult(ast)(tail(f.as))
    assertResult(bst)(tail(f.bs))
  }

  it should "throw errors when trying to access the head or tail of a Nil list" in {
    intercept[java.util.NoSuchElementException](head(Nil))
    intercept[java.lang.UnsupportedOperationException](tail(Nil))
  }

}
