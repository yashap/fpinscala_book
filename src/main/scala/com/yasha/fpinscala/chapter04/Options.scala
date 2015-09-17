package com.yasha.fpinscala.chapter04

object Options {

  sealed trait Option[+A] {

    /** For fun */
    def isDefined: Boolean = this match {
      case Some(_) => true
      case _ => false
    }

    /**
     * Exercise 4.1
     *
     * Implement map, flatMap, getOrElse, orElse and filter on Option
     */
    def map[B](f: A => B): Option[B] = this match {
      case Some(a) => Some(f(a))
      case _ => None
    }

    def flatMap[B](f: A => Option[B]): Option[B] = this match {
      case Some(a) => f(a)
      case _ => None
    }

    /** Note: [B >: A] means B must be A or a supertype of A (the type of the Option) */
    def getOrElse[B >: A](default: => B): B = this match {
      case Some(a) => a
      case _ => default
    }

    def orElse[B >: A](ob: => Option[B]): Option[B] = this match {
      case a: Some[A] => a
      case _ => ob
    }

    def filter(f: A => Boolean): Option[A] = this match {
      case Some(a) if f(a) => Some(a)
      case _ => None
    }
  }

  case class Some[+A](get: A) extends Option[A]
  case object None extends Option[Nothing]

  /**
   * Exercise 4.2
   *
   * Implement the variance function in terms of flatMap. If the mean of a sequence is
   * m, the variance is the mean of math.pow(x - m, 2) for each element x in the
   * sequence. See the definition of variance on Wikipedia (http://mng.bz/0Qsr).
   */
  def mean(xs: Seq[Double]): Option[Double] = xs.length match {
    case 0 => None
    case l => Some(xs.sum / l)
  }

  def variance(xs: Seq[Double]): Option[Double] =
    mean(xs).flatMap{ meanOfSeq =>
      mean(xs.map{ elem =>
        math.pow(elem - meanOfSeq, 2)
      })
    }

  /**
   * Cool function from the notes. Takes any other function, and lifts it into a new
   * function that operates on Options
   */
  def lift[A, B](f: A => B): Option[A] => Option[B] =
    (a: Option[A]) => a.map(f)

  /**
   * Exercise 4.3
   *
   * Write a generic function map2 that combines two Option values using a binary
   * function. If either Option value is None, then the return value is too.
   */
  def map2[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = (a, b) match {
    case (Some(x), Some(y)) => Some(f(x, y))
    case _ => None
  }

  /** For fun */
  def lift2[A, B, C](f: (A, B) => C): (Option[A], Option[B]) => Option[C] =
    (a: Option[A], b: Option[B]) => map2(a, b)(f)

  /**
   * Exercise 4.4
   *
   * Write a function sequence that combines a list of Options into one Option containing
   * a list of all the Some values in the original list. If the original list contains
   * None even once, the result of the function should be None; otherwise the result
   * should be Some with a list of all the values.
   */
  def sequence[A](as: List[Option[A]]): Option[List[A]] = as match {
    case Nil => Some(Nil)
    case opt :: opts => opt.flatMap{ x =>  // any time opt is None, this will short circuit to None
      sequence(opts).map{ (xs: List[A]) =>
        x :: xs
      }
    }
  }

  /**
   * Exercise 4.5
   *
   * Sometimes we’ll want to map over a list using a function that might fail, returning
   * None if applying it to any element of the list returns None.
   *
   * For example, what if we have a whole list of String values that we wish to parse to
   * Option[Int]? In that case, we can simply sequence the results of the map:
   *
   * def parseInts(a: List[String]): Option[List[Int]] =
   *   sequence(a.map(i => Try(i.toInt)))
   * // Try(i.toInt)) turns a String into an Option[Int]
   * // a.map(i => Try(i.toInt)) turns a List[String] into a List[Option[Int]]
   * // sequencing this turns it into an Option[List[Int]]
   *
   * Unfortunately, this is inefficient, since it traverses the list twice, first to
   * convert each String to an Option[Int], and a second pass to combine these Option[Int]
   * values into an Option[List[Int]]. Wanting to sequence the results of a map this way
   * is a common enough occurrence to warrant a new generic function, traverse, with the
   * following signature:
   *
   * def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]]
   *
   * Implement this function. It’s straightforward to do using map and sequence, but try
   * for a more efficient implementation that only looks at the list once. In fact,
   * implement sequence in terms of traverse.
   */
  def traverse[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = as match {
    case Nil => Some(Nil)
    case x :: xs => map2(f(x), traverse(xs)(f))((b: B, bs: List[B]) => b :: bs)
  }

  def sequenceInTraverse[A](as: List[Option[A]]): Option[List[A]] =
    traverse(as)(identity)

}
