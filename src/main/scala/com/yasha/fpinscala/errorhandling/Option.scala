package com.yasha.fpinscala.errorhandling

import scala.{Option => _, Some => _, Either => _, None => _, _}
// hide std library stuff where we'll be writing our own

trait Option[+A] {

  def map[B](f: A => B): Option[B] =
    this match {
      case None => None
      case Some(a) => Some(f(a))
    }

  def getOrElse[B>:A](default: => B): B =
    this match {
      case None => default
      case Some(a) => a
    }

  def flatMap[B](f: A => Option[B]): Option[B] =
    this.map(f).getOrElse(None)
    // Without the getOrElse, this.map(f) would return:
    // Option[Option[B]]
    // So we're taking a function that turns whatever into Options
    // And sort of flattening the result

  def orElse[B>:A](ob: => Option[B]): Option[B] =
    this.map(Some(_)).getOrElse(ob)

  def filter(f: A => Boolean): Option[A] =
    this match {
      case Some(a) if f(a) => this
      case _ => None
    }

}

case class Some[+A](get: A) extends Option[A]
case object None extends Option[Nothing]


object Option {

  def mean(xs: Seq[Double]): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)

  def variance(xs: Seq[Double]): Option[Double] =
    mean(xs) flatMap (m => mean( xs map (x => math.pow(x - m, 2)) ))
    // *** My initial solution ***
    // if (xs.isEmpty) None
    // else {
    //   val m = mean(xs).getOrElse(0.0)
    //   mean( xs map (x => math.pow(x - m, 2)) )
    // }

  def Try[A](a: => A): Option[A] =
    try Some(a)
    catch { case e: Exception => None }

  def map2[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    a flatMap (aa => b map (bb => f(aa, bb)))

  def map2FC[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    for {
      aa <- a
      bb <- b
    } yield f(aa, bb)

  def insuranceRateQuote(age: Int, numberOfSpeedingTickets: Int): Double =
    100.0 - age + numberOfSpeedingTickets * 5

  def parseInsuranceRateQuote(age: String, numberOfSpeedingTickets: String): Option[Double] = {
    val optAge: Option[Int] = Try(age.toInt)
    val optTickets: Option[Int] = Try(numberOfSpeedingTickets.toInt)
    map2(optAge, optTickets)(insuranceRateQuote)
  }

  // Turns List[Option[A]] into Option[List[A]] *IF* every Option[A] is Some[A]
  // If any are None, returns None
  def sequence[A](as: List[Option[A]]): Option[List[A]] =
    as match {
      case Nil => Some(Nil)
      case h :: t => h flatMap (hh => sequence(t) map (hh :: _))
      // We use flatMap, not map, because h is Option[A],
      // and we don't want an Option[Option[whatever]] scenario
      // So we take our head, and turn it into ... {{ figure out later }}
    }

  def traverse[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] =
    as match {
      case Nil => Some(Nil)
      case h :: t => map2(f(h), traverse(t)(f))(_ :: _)
    }

}
