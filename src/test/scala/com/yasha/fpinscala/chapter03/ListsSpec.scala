package com.yasha.fpinscala.chapter03

import com.yasha.fpinscala.UnitSpec

class ListsSpec extends UnitSpec {
  import Lists._
  import List._

  val intList: List[Int] = List(10, 11, 2)
  val stringList: List[String] = List("one", "two", "three")

  "tail" should "return the tail of a non-empty list" in {
    tail(intList) shouldBe List(11, 2)
  }
  it should "throw UnsupportedOperationException on an empty list" in {
    a [UnsupportedOperationException] should be thrownBy {
      tail(Nil)
    }
  }

  "setHead" should "replace the head for a non-empty list" in {
    setHead(stringList, "new head") shouldBe List("new head", "two", "three")
  }
  it should "throw UnsupportedOperationException on an empty list" in {
    a[UnsupportedOperationException] should be thrownBy {
      setHead(Nil, 1)
    }
  }

  "drop" should "drop the first n elements" in {
    drop(intList, 2) shouldBe List(2)
  }
  it should "return the empty list if you try to drop too many elements" in {
    drop(intList, 10) shouldBe Nil
  }

  "dropWhile" should "drop elements while the predicate is true on the head" in {
    dropWhile(intList, (x: Int) => x > 5) shouldBe List(2)
    dropWhile(intList, (x: Int) => x > 100) shouldBe intList
  }
  it should "return the empty list if the predicate is always true" in {
    dropWhile(intList, (x: Int) => x > -1) shouldBe Nil
  }

  "init" should "drop the last element on non-empty lists" in {
    init(intList) shouldBe List(10, 11)
    init(List("a")) shouldBe Nil
  }
  it should "throw UnsupportedOperationException on an empty list" in {
    a[UnsupportedOperationException] should be thrownBy {
      init(Nil)
    }
  }

  "lengthFunc" should "calculate the length of a list" in {
    lengthFunc(intList) shouldBe 3
    lengthFunc(Nil) shouldBe 0
  }

  "sumFL" should "sum the elements of a list" in {
    sumFL(intList) shouldBe 23
    sumFL(Nil) shouldBe 0
  }

  "productFL" should "multiply the elements of a list" in {
    productFL(intList) shouldBe 220
    productFL(Nil) shouldBe 1
  }

  "lengthFL" should "return the length of a list" in {
    lengthFL(intList) shouldBe 3
    lengthFL(Nil) shouldBe 0
  }

  "reverse" should "reverse lists with 2+ elements" in {
    reverse(intList) shouldBe List(2, 11, 10)
  }
  it should "not affect lists with 0-1 elements" in {
    reverse(Nil) shouldBe Nil
    reverse(List(100)) shouldBe List(100)
  }

  "foldLeftInFR" should "still work like a foldLeft" in {
    foldLeftInFR(intList, 0)(_ + _) shouldBe 23
    foldLeftInFR(Nil: List[Int], 0)(_ + _) shouldBe 0
    foldLeftInFR(intList, 1)(_ * _) shouldBe 220
    foldLeftInFR(Nil: List[Int], 1)(_ * _) shouldBe 1
  }

  "foldRightInFL" should "still work like a foldRight" in {
    foldRightInFL(intList, 0)(_ + _) shouldBe 23
    foldRightInFL(Nil: List[Int], 0)(_ + _) shouldBe 0
    foldRightInFL(intList, 1)(_ * _) shouldBe 220
    foldRightInFL(Nil: List[Int], 1)(_ * _) shouldBe 1
  }

  "appendElement" should "append an element to the end of a list" in {
    appendElement(intList, 100) shouldBe List(10, 11, 2, 100)
    appendElement(Nil, 100) shouldBe List(100)
  }

  "appendList" should "append a list to the end of a list" in {
    val intList2 = List(100, 101)

    appendList(intList, intList2) shouldBe List(10, 11, 2, 100, 101)
    appendList(Nil, intList2) shouldBe intList2
    appendList(intList, Nil) shouldBe intList
  }

  "addOne" should "add one to every element of a list of int" in {
    addOne(intList) shouldBe List(11, 12, 3)
    addOne(Nil) shouldBe Nil
  }

  "mapDoublesToStrings" should "turn a list of double into a matching list of string" in {
    mapDoublesToStrings(List(11.1, 22.2)) shouldBe List("11.1", "22.2")
    mapDoublesToStrings(Nil) shouldBe Nil
  }

  "map" should "apply a function to each element in a list" in {
    map(intList)(_ * 10) shouldBe List(100, 110, 20)
    map(stringList)(_.head) shouldBe List('o', 't', 't')
  }

  "filter" should "filter a list based on a predicate" in {
    filter(intList)(_ > 5) shouldBe List(10, 11)
    filter(intList)(_ < 5) shouldBe List(2)
    filter(intList)(_ < 0) shouldBe Nil
    filter(Nil: List[Int])(_ > 0) shouldBe Nil
  }

  "flatMap" should "map a function to a list, creating a list of lists, then flatten it" in {
    flatMap(intList)((a) => List(a.toString, (a * 10).toString)) shouldBe List("10", "100", "11", "110", "2", "20")
    flatMap(Nil: List[Int])((a) => List(a.toString, (a * 10).toString)) shouldBe Nil
  }

  "filterFM" should "filter a list based on a predicate" in {
    filterFM(intList)(_ > 5) shouldBe List(10, 11)
    filterFM(intList)(_ < 5) shouldBe List(2)
    filterFM(intList)(_ < 0) shouldBe Nil
    filterFM(Nil: List[Int])(_ > 0) shouldBe Nil
  }

  "addElements" should "add the elements in two lists" in {
    val intList2 = List(100, 100, 100)

    addElements(intList, intList2) shouldBe List(110, 111, 102)
  }
  it should "truncate to the length of the shorter list" in {
    val intList2 = List(100, 100, 100, 100, 100)

    addElements(intList, intList2) shouldBe List(110, 111, 102)
    addElements(intList2, intList) shouldBe List(110, 111, 102)
    addElements(Nil, intList) shouldBe Nil
    addElements(intList, Nil) shouldBe Nil
    addElements(Nil, Nil) shouldBe Nil
  }

  "zipWith" should "combine the elements in two lists with a provided function" in {
    val intList2 = List(100, 100, 100)

    zipWith(intList, intList2)(_ + _) shouldBe List(110, 111, 102)
  }
  it should "truncate to the length of the shorter list" in {
    val intList2 = List(100, 100, 100, 100, 100)

    zipWith(intList, intList2)(_ + _) shouldBe List(110, 111, 102)
    zipWith(intList2, intList)(_ + _) shouldBe List(110, 111, 102)
    zipWith(Nil: List[Int], intList)(_ + _) shouldBe Nil
    zipWith(intList, Nil: List[Int])(_ + _) shouldBe Nil
    zipWith(Nil: List[Int], Nil: List[Int])(_ + _) shouldBe Nil
  }

  "take" should "take up to n items from a list" in {
    take(intList, 2) shouldBe List(10, 11)
    take(intList, 3) shouldBe intList
    take(intList, 4) shouldBe intList
    take(Nil, 2) shouldBe Nil
  }

  "startsWith" should "test if the first list starts with the second" in {
    startsWith(intList, intList) shouldBe true
    startsWith(intList, List(10, 11)) shouldBe true
    startsWith(intList, List(10, 11, 2, 2)) shouldBe false
    startsWith(intList, List(11, 2)) shouldBe false
  }

  "hasSubsequence" should "test if a list has sub-list" in {
    hasSubsequence(intList, intList) shouldBe true
    hasSubsequence(intList, List(10, 11)) shouldBe true
    hasSubsequence(intList, List(11, 2)) shouldBe true
    hasSubsequence(intList, List(11)) shouldBe true
    hasSubsequence(intList, Nil) shouldBe true
    hasSubsequence(Nil, Nil) shouldBe true

    hasSubsequence(intList, List(11, 10)) shouldBe false
    hasSubsequence(intList, List(10, 11, 2, 2)) shouldBe false
    hasSubsequence(intList, List(12)) shouldBe false
  }
}
