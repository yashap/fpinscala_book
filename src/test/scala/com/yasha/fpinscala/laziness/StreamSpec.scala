//package com.yasha.fpinscala.laziness
//
//import com.yasha.fpinscala.UnitSpec
//import scala.language.reflectiveCalls
//
//class StreamSpec extends UnitSpec {
//
//  def fixture =
//    new {
//      val as: Stream[Int] = Stream(10, 20, 30, 2, 500, 1)
//      val bs: Stream[Double] = Stream(10.1, 20.1, 30.1, 2.1, 500.1, 1.5)
//    }
//
//  "A Steam" should "have a head equal to its first element" in {
//    val f = fixture
//    assertResult(Some(10))(f.as.headOption)
//    assertResult(Some(10.1))(f.bs.headOption)
//    assertResult(None)(Stream().headOption)
//  }
//  it should "have a working toList method" in {
//    val f = fixture
//    assertResult(List(10, 20, 30, 2, 500, 1))(f.as.toList)
//    assertResult(List(10.1, 20.1, 30.1, 2.1, 500.1, 1.5))(f.bs.toList)
//  }
//  it should "have a working take method" in {
//    val f = fixture
//    assertResult(List(10, 20))(f.as.take(2).toList)
//    assertResult(List(10.1, 20.1, 30.1, 2.1, 500.1, 1.5))(f.bs.take(15).toList)
//  }
//  it should "have a working drop method" in {
//    val f = fixture
//    assertResult(List(30, 2, 500, 1))(f.as.drop(2).toList)
//    assertResult(Nil)(f.bs.drop(15).toList)
//  }
//  it should "have a working takeWhile method" in {
//    val f = fixture
//    assertResult(List(10, 20))(f.as.takeWhile(_ < 25).toList)
//    assertResult(f.bs.toList)(f.bs.takeWhile(_ < 1000.0).toList)
//  }
//  it should "have a working forAll method" in {
//    val f = fixture
//    assertResult(true)(f.as.forAll(_ < 501))
//    assertResult(true)(f.bs.forAll(_ > 1.0))
//    assertResult(false)(f.bs.forAll(_ > 2.0))
//  }
//  it should "have a working takeWhileFR method" in {
//    val f = fixture
//    assertResult(List(10, 20))(f.as.takeWhileFR(_ < 25).toList)
//    assertResult(f.bs.toList)(f.bs.takeWhileFR(_ < 1000.0).toList)
//    assertResult(None)(Stream().headOption)
//  }
//  it should "have a working map method" in {
//    val f = fixture
//    assertResult(List(20, 40, 60, 4, 1000, 2))(f.as.map(_ * 2).toList)
//    assertResult(List(20.1, 30.1, 40.1, 12.1, 510.1, 11.5))(f.bs.map(_ + 10.0).toList)
//  }
//  it should "have a working filter method" in {
//    val f = fixture
//    assertResult(List(1))(f.as.filter(_ % 2 == 1).toList)
//    assertResult(List(30.1, 500.1))(f.bs.filter(_ > 25.0).toList)
//    assertResult(Nil)(f.bs.filter(_ > 1000.0).toList)
//  }
//  it should "have a working append method" in {
//    val f = fixture
//    assertResult(List(10, 20, 30, 2, 500, 1, 10))(f.as.append(Stream(10)).toList)
//    assertResult(List(10, 20, 30, 2, 500, 1))(f.as.append(Stream()).toList)
//    assertResult{
//      List(10.1, 20.1, 30.1, 2.1, 500.1, 1.5, 1000.0, 10000.0)
//    } {
//      f.bs.append(Stream(1000.0, 10000.0)).toList
//    }
//  }
//  it should "have a working flatMap method" in {
//    val f = fixture
//    assertResult(List(20, 40, 60, 4, 1000, 2))(f.as.flatMap(a => Stream(a * 2)).toList)
//    assertResult(Nil)(Stream().flatMap(a => Stream(a)).toList)
//    assertResult(List(20.1, 30.1, 40.1, 12.1, 510.1, 11.5))(f.bs.flatMap(b => Stream(b + 10.0)).toList)
//  }
//  it should "have a working startsWith method" in {
//    val f = fixture
//    assertResult(true)(f.as.startsWith(Stream(10, 20, 30)))
//    assertResult(true)(f.as.startsWith(f.as))
//    assertResult(true)(f.as.startsWith(Stream()))
//    assertResult(false)(f.as.startsWith(Stream(10, 20, 31)))
//    assertResult(false)(Stream(1, 2).startsWith(Stream(1, 2, 3)))
//  }
//  it should "have a working tails method" in {
//    assertResult{
//      Stream(Stream(1,2,3), Stream(2,3), Stream(3), Stream()).toList.map(_.toList)
//    } { Stream(1,2,3).tails.toList.map(_.toList) }
//    assertResult{
//      Stream(Stream()).toList.map(_.toList)
//    } { Stream().tails.toList.map(_.toList) }
//  }
//  it should "have a working hasSubsequence method" in {
//    val f = fixture
//    assertResult(true)(f.as.hasSubsequence(Stream(20, 30)))
//    assertResult(true)(f.as.hasSubsequence(Stream()))
//    assertResult(false)(f.as.hasSubsequence(Stream(30, 20)))
//  }
//  it should "have a working scanRight method" in {
//    assertResult(List(6,5,3,0))(Stream(1,2,3).scanRight(0)(_ + _).toList)
//  }
//
//  "ones" should "produce an infinite stream of the same value" in {
//    assertResult(List(1, 1, 1, 1))(Stream.ones.take(4).toList)
//    assertResult(List(1, 1, 1, 1))(Stream.onesU.take(4).toList)
//  }
//
//  "constant" should "produce an infinite stream of the same value" in {
//    assertResult(List(3, 3, 3, 3))(Stream.constant(3).take(4).toList)
//    assertResult(List(3, 3, 3, 3))(Stream.constantU(3).take(4).toList)
//  }
//
//  "from" should "produce an infinite stream counting up from a value" in {
//    assertResult(List(2, 3, 4, 5, 6))(Stream.from(2).take(5).toList)
//    assertResult(List(2, 3, 4, 5, 6))(Stream.fromU(2).take(5).toList)
//  }
//
//  "fibs" should "be a stream of the Fibonacci numbers" in {
//    assertResult(List(0, 1, 1, 2, 3, 5, 8, 13))(Stream.fibs.take(8).toList)
//    assertResult(List(0, 1, 1, 2, 3, 5, 8, 13))(Stream.fibsU.take(8).toList)
//  }
//
//  "unfold" should "produce infinite streams based on a starting value and a function" in {
//    assertResult{
//      List(11, 21, 31, 41, 51)
//    } {
//      Stream.unfold(10)(x => Option((x + 1, x + 10))).take(5).toList
//    }
//  }
//
//
//}
