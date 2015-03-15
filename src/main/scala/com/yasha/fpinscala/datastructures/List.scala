package com.yasha.fpinscala.datastructures

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {

  def head[A](as: List[A]): A =
    as match {
      case Nil => throw new java.util.NoSuchElementException("head of empty list")
      case Cons(x, xy) => x
    }

  def tail[A](as: List[A]): List[A] =
    as match {
      case Nil => throw new java.lang.UnsupportedOperationException("tail of empty list")
      case Cons(x, xy) => xy
    }

  def setHead[A](as: List[A], h: A): List[A] =
    as match {
      case Nil => Cons(h, Nil)
      case Cons(x, xs) => Cons(h, Cons(x, xs))
    }

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match {
      case Nil => a2
      case Cons(h,t) => Cons(h, append(t, a2))
    }

  def sum(ints: List[Int]): Int =
    ints match {
      case Nil => 0
      case Cons(x,xs) => x + sum(xs)
    }

  def product(ds: List[Double]): Double =
    ds match {
      case Nil => 1.0
      case Cons(0.0, _) => 0.0
      case Cons(x,xs) => x * product(xs)
    }

  def sum2(ns: List[Int]) =
    foldRight(ns, 0)((x,y) => x + y)

  def product2(ns: List[Double]) =
    foldRight(ns, 1.0)(_ * _)

  def drop[A](as: List[A], n: Int): List[A] = {
    as match {
      case Nil => Nil
      case Cons(x, xs) if n > 0 => drop(xs, n - 1)
      case Cons(y, ys) => Cons(y, ys)
    }
  }

  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
    case Cons(x, xs) if f(x) => dropWhile(xs, f)
    case _ => l
  }

  def init[A](l: List[A]): List[A] = l match {
    case Nil => throw new Error("init of Nil")
    case Cons(_, Nil) => Nil
    case Cons(y, ys) => Cons(y, init(ys))
  }

  def foldRight[A,B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def sumRight(xs: List[Int]) =
    foldRight(xs, 0)(_ + _)

  def productRight(xs: List[Double]) =
    foldRight(xs, 1.0)(_ * _)

  def lengthRight[A](as: List[A]): Int =
    foldRight(as, 0)((_, acc) => acc + 1)

  def length[A](as: List[A]): Int = {
    def loop(xs: List[A], n: Int): Int = xs match {
      case Nil => n
      case Cons(y, ys) => loop(ys, n + 1)
    }
    loop(as, 0)
  }

  @annotation.tailrec
  def foldLeft[A,B](as: List[A], z: B)(f: (B, A) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
    }

  def sumLeft(xs: List[Int]) =
    foldLeft(xs, 0)(_ + _)

  def productLeft(xs: List[Double]) =
    foldLeft(xs, 1.0)(_ * _)

  def lengthLeft[A](as: List[A]): Int =
    foldLeft(as, 0)((acc, _) => acc + 1)

  def map[A,B](as: List[A])(f: A => B): List[B] =
    foldRight(as, Nil: List[B])((h, t) => Cons(f(h), t))
}
