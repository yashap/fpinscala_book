package com.yasha.fpinscala.errorhandling

import com.yasha.fpinscala.UnitSpec
import scala.language.reflectiveCalls

class OptionSpec extends UnitSpec {
  import scala.{Option => _, Some => _, Either => _, None => _, _}
  // hide std library stuff where we'll be writing our own
  import Option._

  def fixture =
    new {
      val some10 = Some(10)
      val some20 = Some(20)
      val ds = List(9.0, 4.0, 2.0)
    }

  // Testing Option methods
  "An Option trait" should "have a working map method" in {
    val f = fixture
    assertResult( f.some20 )( f.some10 map (_ + 10) )
    assertResult( f.some10 )( f.some20 map (_ - 10) )
  }
  it should "have a working flatMap method" in {
    val f = fixture
    def intOver15(x: Int): Option[Int] = if (x > 15) Some(x) else None
    assertResult( None )( f.some10 flatMap intOver15 )
    assertResult( f.some20 )( f.some20 flatMap intOver15 )
  }
  it should "have a working getOrElse method" in {
    val f = fixture
    assertResult( 10 )( f.some10 getOrElse 10 )
    assertResult( 20 )( f.some20 getOrElse 20 )
    assertResult( -1 )( None getOrElse -1 )
  }
  // TODO: test orElse
  // TODO: test filter

  // Tests where we use Option in functions
  "mean" should "return the proper Option" in {
    val f = fixture
    assertResult( Some(5.0) )( mean(f.ds) )
    assertResult( None )( mean(Nil) )
  }

  "variance" should "return None for empty Seq" in {
    assertResult( None )( variance(Nil) )
  }
  it should "return proper values for non-empty Seq" in {
    val f = fixture
    def avg(ds: Seq[Double]): Double = ds.sum / ds.length
    assertResult( Some(avg(f.ds map (x => math.pow(x - avg(f.ds), 2)))) ) {
      variance(f.ds)
    }
  }

  "map2" should "be able to lift 'normal' functions to work with Options" in {
    val f = fixture
    def sum(a: Int, b: Int): Int = a + b
    assertResult( Some(30) )( map2(f.some10, f.some20)(sum) )
  }

  "parseInsuranceRateQuote" should "return null if the strings can't be parsed" in {
    //parseInsuranceRateQuote(age: String, numberOfSpeedingTickets: String): Option[Double]
    assertResult( None )( parseInsuranceRateQuote("thirty", "5") )
  }
  it should "return the proper value if the strings can be parsed" in {
    val age = "30"
    val numTickets = "5"
    assertResult( Some(100.0 - age.toInt + numTickets.toInt * 5) ) {
      parseInsuranceRateQuote(age, numTickets)
    }
  }

  // TODO: test sequence

  // TODO: test traverse

}
