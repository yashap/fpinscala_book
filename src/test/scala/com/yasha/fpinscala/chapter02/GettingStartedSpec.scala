package com.yasha.fpinscala.chapter02

import com.yasha.fpinscala.UnitSpec

class GettingStartedSpec extends UnitSpec {
  import GettingStarted._

  val isIncreasingFloats = (a: Float, b: Float) => a <= b
  val isIncreasingStrings = (a: String, b: String) => a <= b
  val isIncreasingInts = (a: Int, b: Int) => a <= b

  "fib" should "return the proper value for any fibonacci number request" in {
    val fibs: List[(Int, Int)] = List(0, 1, 1, 2, 3, 5, 8, 13, 21, 34).zipWithIndex

    fibs.foreach { case (item, idx) =>
      assert(fib(idx) == item)
    }
  }

  "isSorted" should "return true for empty arrays" in {
    isSorted(Array(), isIncreasingFloats) shouldBe true
  }
  it should "return true for arrays of length 1" in {
    isSorted(Array("hi"), isIncreasingStrings) shouldBe true
  }
  it should "return true for sorted arrays of length > 1" in {
    isSorted(Array("aa", "ab"), isIncreasingStrings) shouldBe true
    isSorted(Array(10, 11, 12), isIncreasingInts) shouldBe true
  }
  it should "return false for unsorted arrays of length > 1" in {
    isSorted(Array("ab", "aa"), isIncreasingStrings) shouldBe false
    isSorted(Array(10, 12, 11), isIncreasingInts) shouldBe false
  }

  "curry" should "not alter the effect of a function" in {
    val uncurriedSum = (a: Int, b: Int) => a + b
    val curriedSum = curry(uncurriedSum)

    curriedSum(2)(12) shouldBe uncurriedSum(2, 12)
  }

  "uncurry" should "not alter the effect of a function" in {
    val curriedSum = (a: Int) => (b: Int) => a + b
    val uncurriedSum = uncurry(curriedSum)

    uncurriedSum(2, 12) shouldBe curriedSum(2)(12)
  }

  "compose" should "work like andThen from the standard lib" in {
    val stringLength = (a: String) => a.length
    val timesTen = (b: Int) => b * 10

    val composed = compose(timesTen, stringLength)
    val composedStdLib = stringLength andThen timesTen

    composed("hello") shouldBe composedStdLib("hello")
    composed("") shouldBe composedStdLib("")
  }

}
