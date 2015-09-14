package com.yasha.fpinscala.state


trait InelegantRNG {

  import InelegantRNG._

  def nextInt: (Int, InelegantRNG)

  def nonNegativeInt: (Int, InelegantRNG) = {
    val (i, r) = nextInt

    if (i == Int.MinValue) (-(i + 1), r)
    else if (i < 0) (-i, r)
    else (i, r)
  }

  def double: (Double, InelegantRNG) = {
    val (i, r) = nonNegativeInt
    (i.toDouble / (Int.MaxValue.toDouble + 1.0), r)
  }

  def intDouble: ((Int, Double), InelegantRNG) = {
    val (i, r) = nextInt
    val (d, _) = double
    ((i, d), r)
  }

  def doubleInt: ((Double, Int), InelegantRNG) = {
    val ((i, d), r) = intDouble
    ((d, i), r)
  }

  def double3: ((Double, Double, Double), InelegantRNG) = {
    val (d, r) = double
    val (d2, r2) = r.double
    val (d3, r3) = r2.double
    ((d, d2, d3), r3)
  }

  def ints(count: Int): (List[Int], InelegantRNG) =
    nextInt match {
        case (i, _) if count == 0 => (Nil, this)
        case (i, r) => {
          val (xs, r1) = r.ints(count - 1)
          (i :: xs, r)
        }
      }

}


object InelegantRNG {

  case class SimpleRNG(seed: Long) extends InelegantRNG {

    def nextInt: (Int, InelegantRNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      val nextRNG = SimpleRNG(newSeed)
      val n = (newSeed >>> 16).toInt
      (n, nextRNG)
    }

  }

}