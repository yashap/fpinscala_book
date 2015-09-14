package com.yasha.fpinscala.state

trait RNG {

  def nextInt: (Int, RNG)

}


object RNG {

  case class SimpleRNG(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      val nextRNG = SimpleRNG(newSeed)
      val n = (newSeed >>> 16).toInt
      (n, nextRNG)
    }
  }

  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (i, r) = rng.nextInt

    if (i == Int.MinValue) (-(i + 1), r)
    else if (i < 0) (-i, r)
    else (i, r)
  }

  def double(rng: RNG): (Double, RNG) = {
    val (i, r) = nonNegativeInt(rng)
    (i / (Int.MaxValue.toDouble + 1), r)
  }

  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i, r) = rng.nextInt
    val (d, r2) = double(r)
    ((i, d), r2)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val ((i, d), r) = intDouble(rng)
    ((d, i), r)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d, r) = double(rng)
    val (d2, r2) = double(r)
    val (d3, r3) = double(r2)
    ((d, d2, d3), r3)
  }

  def ints(count: Int)(rng: RNG): (List[Int], RNG) =
    rng.nextInt match {
      case (i, _) if count == 0 => (Nil, rng)
      case (i, r) => {
        val (is, r2) = ints(count - 1)(r)
        (i :: is, r2)
      }
    }

  /**
   * Note that all of the above are functions that take an RNG, and return a pair
   * of some type and an RNG.
   * We can make this explicit with a new type
   */

  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] =
    rng => (a, rng)

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
    }
  /**
   * Example usage:
   * val rng = SimpleRNG(42)
   * def intDivedByTwo: Rand[Int] = map(int)(_ / 2)
   * intDivedByTwo(rng)
   */

  def nonNegativeEven: Rand[Int] =
    map(nonNegativeInt)(i => i - i % 2)

  def doubleMap(rng: RNG): Rand[Double] =
    map(nonNegativeInt)(i => i.toDouble / (Int.MaxValue.toDouble + 1.0))

  def map2[A,B,C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    rng => {
      val (a, rng2) = ra(rng)
      val (b, rng3) = rb(rng2)
      (f(a, b), rng3)
    }

  val myIntDouble: Rand[(Int, Double)] =
    map2(int, double)((_, _))

  /**
   * Or, more generally
   */

  def both[A, B](ra: Rand[A], rb: Rand[B]): Rand[(A, B)] =
    map2(ra, rb)((_, _))

  val randIntDouble: Rand[(Int, Double)] =
    both(int, double)

  val randDoubleInt: Rand[(Double, Int)] =
    both(double, int)

  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] =
    fs.foldRight(unit(List[A]()))((f, acc) => map2(f, acc)(_ :: _))

  def intsS(count: Int): Rand[List[Int]] =
    sequence(List.fill(count)(int))

  def flatMap[A,B](f: Rand[A])(g: A => Rand[B]): Rand[B] =
    rng => {
      val (a, rng2) = f(rng)
      val f2 = g(a)
      f2(rng2)
    }

  def nonNegativeLessThan(n: Int): Rand[Int] =
    flatMap(nonNegativeInt){ i =>
      val mod = i % n
      if (i + (n-1) - mod >= 0) unit(mod)
      else nonNegativeLessThan(n)
    }

  def mapF[A, B](s: Rand[A])(f: A => B): Rand[B] =
    flatMap(s)(a => unit(f(a)))

  def map2F[A,B,C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    flatMap(ra)(a => map(rb)(b => f(a, b)))

}

/**
 * Left off on page 87, after exercise 6.9
 */
