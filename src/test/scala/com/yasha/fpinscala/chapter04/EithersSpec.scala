package com.yasha.fpinscala.chapter04

import com.yasha.fpinscala.UnitSpec

class EithersSpec extends UnitSpec {
  import Eithers._

  val right: Either[Exception, Int] = Right(10)
  val left: Either[Exception, Int] = Left(new Exception("oh shit"))

  val rightDefault: Either[Exception, Int] = Right(-1)

  val otherRight: Either[Exception, Int] = Right(100)
  val otherLeft: Either[Exception, Int] = Left(new Exception("oh snap"))

  "map" should "apply a function to the value if it's wrapped in Right" in {
    right.map(_ * 5) shouldBe Right(50)
  }
  it should "do nothing to a value wrapped in left" in {
    left.map(_ * 5) shouldBe left
  }

  "flatMap" should "apply a function to the value if it's wrapped in Right, then flatten" in {
    right.flatMap(a => Right(a * 5)) shouldBe Right(50)
  }
  it should "do nothing to a value wrapped in left" in {
    left.flatMap(a => Right(a * 5)) shouldBe left
  }

  "orElse" should "do nothing to a Right" in {
    right.orElse(rightDefault) shouldBe right
  }
  it should "turn a Left into a default" in {
    left.orElse(rightDefault) shouldBe rightDefault
  }

  "map2" should "apply a function with arity 2 to 2 Rights" in {
    right.map2(otherRight)(_ + _) shouldBe Right(110)
  }
  it should "do nothing if either is a Left" in {
    left.map2(otherLeft)(_ + _) shouldBe left
    left.map2(otherRight)(_ + _) shouldBe left
    right.map2(otherLeft)(_ + _) shouldBe otherLeft
  }

}
