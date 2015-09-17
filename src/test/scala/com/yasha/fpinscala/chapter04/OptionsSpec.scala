package com.yasha.fpinscala.chapter04

import com.yasha.fpinscala.UnitSpec

class OptionsSpec extends UnitSpec {
  import Options._

  val noInt: Option[Int] = None

  "Option" should "have a working map method" in {
    Some(20).map(_ * 3) shouldBe Some(60)
    noInt.map(_ * 3) shouldBe None
  }
  it should "have a working flatMap method" in {
    Some(20).flatMap((a: Int) => Some(a * 3)) shouldBe Some(60)
    noInt.flatMap((a: Int) => Some(a * 3)) shouldBe None
  }
  it should "have a working getOrElse method" in {
    Some(20).getOrElse(-1) shouldBe 20
    noInt.getOrElse(-1) shouldBe -1
  }
  it should "have a working orElse method" in {
    Some(20).orElse(Some(-1)) shouldBe Some(20)
    noInt.orElse(Some(-1)) shouldBe Some(-1)
  }
  it should "have a working filter method" in {
    Some(20).filter(_ > 10) shouldBe Some(20)
    Some(2).filter(_ > 10) shouldBe None
    noInt.filter(_ > 10) shouldBe None
  }

  "variance" should "calculate the statistical variance of a sequence of doubles" in {
    variance(List()) shouldBe None
    variance(List(10.0, 10.0)) shouldBe Some(0.0)
    variance(List(10.0, 20.0)) shouldBe Some((math.pow(10.0 - 15.0, 2) + math.pow(20.0 - 15.0, 2)) / 2)
  }

  "lift" should "lift a function from A => B to Option[A] => Option[B]" in {
    lift((a: Int) => a + 10)(Some(100)) shouldBe Some(110)
    lift((a: Int) => a + 10)(None) shouldBe None
  }

  "map2" should "take a function of (A, B) => C, lift it to work on Options, and eval it" in {
    val f = (a: Int, b: Int) => (a * b).toString

    map2(Some(10), Some(11))(f) shouldBe Some("110")
    map2(Some(10), None)(f) shouldBe None
    map2(None, Some(11))(f) shouldBe None
    map2(None, None)(f) shouldBe None
  }

  "lift2" should "work like lift, but on a function of (A, B) => C" in {
    val f = (a: Int, b: Int) => (a * b).toString

    lift2(f)(Some(10), Some(11)) shouldBe Some("110")
    lift2(f)(None, Some(11)) shouldBe None
  }

  "sequence" should "combine a list of Options into an option list" in {
    sequence(List(Some(10), Some(11), Some(12))) shouldBe Some(List(10, 11, 12))
    sequence(Nil) shouldBe Some(Nil)
  }
  it should "return None if any of the options are None" in {
    sequence(List(None, None, None)) shouldBe None
    sequence(List(Some(10), None, Some(12))) shouldBe None
  }

  "traverse" should "be able to implement sequence" in {
    sequenceInTraverse(List(Some(10), Some(11), Some(12))) shouldBe sequence(List(Some(10), Some(11), Some(12)))
    sequenceInTraverse(List(Some(10), None, Some(12))) shouldBe sequence(List(Some(10), None, Some(12)))
  }

}
