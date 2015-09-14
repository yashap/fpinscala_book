//package com.yasha.fpinscala.state
//
//import com.yasha.fpinscala.UnitSpec
//import scala.language.reflectiveCalls
//
//
//class InelegantRNGSpec extends UnitSpec {
//
//  def fixture =
//    new {
//      val rng = InelegantRNG.SimpleRNG(42)
//    }
//
//
//  "A RNG" should "create pseudo-random Ints with nextInt in a predictable way" in {
//    val f = fixture
//    val (n1, rng2) = f.rng.nextInt
//    val (n2, rng3) = rng2.nextInt
//
//    assertResult(16159453)(n1)
//    assertResult(-1281479697)(n2)
//  }
//  it should "have a working nonNegativeInt method" in {
//    val f = fixture
//    val (n1, rng2) = f.rng.nonNegativeInt
//    val (n2, rng3) = rng2.nonNegativeInt
//
//    assertResult(16159453)(n1)
//    assertResult(1281479697)(n2)
//  }
//  it should "have a working double method" in {
//    val f = fixture
//    val (n1, rng2) = f.rng.double
//    val (n2, rng3) = rng2.double
//    val (n3, rng4) = rng3.double
//
//    assertResult(true)(n1 >= 0.0 && n1 < 1.0)
//    assertResult(true)(n2 >= 0.0 && n2 < 1.0)
//    assertResult(true)(n3 >= 0.0 && n3 < 1.0)
//  }
//  it should "have working intDouble, doubleInt and double3 methods" in {
//    val f = fixture
//    assertResult(true) {
//      val ((i1, d1), _) = f.rng.intDouble
//      val ((d2, i2), _) = f.rng.doubleInt
//      val ((d3, d4, d5), _) = f.rng.double3
//      val doubles = List(d1, d2, d3, d4, d5)
//      (i1, i2) == (16159453, 16159453) && doubles.min >= 0.0 && doubles.max < 1.0
//    }
//  }
//  it should "have a working ints method" in {
//    val f = fixture
//    assertResult {
//      List(16159453, -1281479697, -340305902, -2015756020, 1770001318)
//    } {
//      f.rng.ints(5)._1
//    }
//  }
//
//}
