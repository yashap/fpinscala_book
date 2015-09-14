//package com.yasha.fpinscala.state
//
//import com.yasha.fpinscala.UnitSpec
//import scala.language.reflectiveCalls
//
//
//class RNGSpec extends UnitSpec {
//  import RNG._
//
//  def fixture =
//    new {
//      val rng = SimpleRNG(42)
//    }
//
//
//  "A RNG" should "create pseudo-random Ints with nextInt in a predictable way" in {
//    val f = fixture
//    val (n1, rng2) = f.rng.nextInt
//    val (n2, _) = rng2.nextInt
//
//    assertResult(16159453)(n1)
//    assertResult(-1281479697)(n2)
//  }
//
//  "nonNegativeInt" should "produce non negative integers" in {
//    val f = fixture
//    val (n1, rng2) = nonNegativeInt(f.rng)
//    val (n2, _) = nonNegativeInt(rng2)
//
//    assertResult(16159453)(n1)
//    assertResult(1281479697)(n2)
//  }
//
//  "double" should "produce random doubles between 0 (inclusive) and 1 (not inclusive)" in {
//    val f = fixture
//    val (n1, rng2) = double(f.rng)
//    val (n2, rng3) = double(rng2)
//    val (n3, _) = double(rng3)
//
//    assertResult(true)(n1 >= 0.0 && n1 < 1.0)
//    assertResult(true)(n2 >= 0.0 && n2 < 1.0)
//    assertResult(true)(n3 >= 0.0 && n3 < 1.0)
//  }
//
//  "intDouble, doubleInt and double3" should "do what their names suggest" in {
//    val f = fixture
//    assertResult(true) {
//      val ((i1, d1), _) = intDouble(f.rng)
//      val ((d2, i2), _) = doubleInt(f.rng)
//      val ((d3, d4, d5), _) = double3(f.rng)
//      val doubles = List(d1, d2, d3, d4, d5)
//      (i1, i2) == (16159453, 16159453) && doubles.min >= 0.0 && doubles.max < 1.0
//    }
//  }
//
//  "ints" should "produce a list of semi-random ints" in {
//    val f = fixture
//    assertResult {
//      List(16159453, -1281479697, -340305902, -2015756020, 1770001318)
//    } {
//      ints(5)(f.rng)._1
//    }
//  }
//
//  "map2" should "be able to be used to implement intDouble" in {
//    val f = fixture
//    assertResult(true) {
//      val ((i, d), r) = intDouble(f.rng)
//      val ((i2, d2), _) = intDouble(r)
//
//      val ((iM, dM), rM) = myIntDouble(f.rng)
//      val ((iM2, dM2), _) = myIntDouble(rM)
//
//      i == iM && i2 == iM2 && d == dM && d2 == dM2
//
//    }
//  }
//
//  "nonNegativeLessThan" should "be work when implemented with flatMap" in {
//    val f = fixture
//    assertResult(true) {
//
//      def testNonNegativeLessThan(n: Int, lt: Int)(rng: RNG): Boolean = {
//
//        def loop(n: Int)(rng: RNG): Boolean = {
//          val (v, r) = nonNegativeLessThan(lt)(rng)
//          if (n <= 0) true
//          else if (v >= lt) false
//          else testNonNegativeLessThan(n - 1, lt)(r)
//        }
//
//        loop(n)(rng)
//      }
//
//      testNonNegativeLessThan(100, 10)(f.rng)
//
//    }
//  }
//
//  "map" should "work the same when implemented with flatMap" in {
//    val f = fixture
//    assertResult(true) {
//
//      val intDivTwo: Rand[Int] = map(int)(_ / 2)
//      val intDivTwoF: Rand[Int] = mapF(int)(_ / 2)
//
//      val (v, r) = intDivTwo(f.rng)
//      val (v2, _) = intDivTwo(r)
//
//      val (vF, rF) = intDivTwoF(f.rng)
//      val (vF2, _) = intDivTwoF(rF)
//
//      v == vF && v2 == vF2
//
//    }
//  }
//
//  "map2" should "work the same when implemented with flatMap" in {
//    // TODO: write tests for map2F
//  }
//
//}
