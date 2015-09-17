package com.yasha.fpinscala.chapter04

object Eithers {

  sealed trait Either[+E, +A] {

    /**
     * Exercise 4.6
     *
     * Implement versions of map, flatMap, orElse, and map2 on Either that operate on the
     * Right value.
     */
    def map[B](f: A => B): Either[E, B] = this match {
      case Right(a) => Right(f(a))
      case Left(e) => Left(e)
    }

    /**
     * NOTE: When mapping over the right side, we must promote the left type parameter to
     * some supertype, to satisfy the +E variance annotation.
     */
    def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match {
      case Right(a) => f(a)
      case Left(e) => Left(e)
    }

    /** Same supertype issue here */
    def orElse[EE >: E,B >: A](b: => Either[EE, B]): Either[EE, B] = this match {
      case Right(a) => Right(a)
      case _ => b
    }

    def map2[EE >: E, B, C](bVal: Either[EE, B])(f: (A, B) => C): Either[EE, C] = (this, bVal) match {
      case (Right(a), Right(b)) => Right(f(a, b))
      case (Right(a), Left(e)) => Left(e)
      case (Left(e), _) => Left(e)        // if both are error, I'll arbitrarily return the first one
    }
  }

  /** When used for success/failure, we'll use left for FAILURE */
  case class Left[+E](value: E) extends Either[E, Nothing]

  /** When used for success/failure, we'll use right (pun!) for SUCCESS */
  case class Right[+A](value: A) extends Either[Nothing, A]

  /** We can then write Try as */
  def Try[A](a: => A): Either[Exception, A] =
    try Right(a)
    catch { case e: Exception => Left(e) }

  /**
   * Exercise 4.7
   *
   * Implement sequence and traverse for Either. These should return the first error
   * that’s encountered, if there is one.
   */
  def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] = ???

  def traverse[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] = ???
}
