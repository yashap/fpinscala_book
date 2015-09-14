package com.yasha.fpinscala.chapter03

import scala.annotation.tailrec

object Lists {

  /**
   * Exercise 3.1
   *
   * What will be the result of the following match expression?
   *
   * val x = List(1,2,3,4,5) match {
   *   case Cons(x, Cons(2, Cons(4, _))) => x
   *   case Nil => 42
   *   case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
   *   case Cons(h, t) => h + sum(t)
   *   case _ => 101
   * }
   *
   * ANSWER:
   * It matches:
   *   case Cons(x, Cons(y, Cons(3, Cons(4, _))))
   * So we have:
   *   x + y
   *   1 + 2
   *   3
   */

  /** provided */
  sealed trait List[+A]
  case object Nil extends List[Nothing]
  case class Cons[+A](head: A, tail: List[A]) extends List[A]

  object List {
    /** provided, not tailrec */
    def apply[A](as: A*): List[A] =
      if (as.isEmpty) Nil
      else Cons(as.head, apply(as.tail: _*))

    /** provided, not tailrec */
    def sum(is: List[Int]): Int = is match {
      case Nil => 0
      case Cons(x, xs) => x + sum(xs)
    }

    /** provided, not tailrec */
    def product(ds: List[Double]): Double = ds match {
      case Nil => 1.0
      case Cons(0.0, _) => 0.0
      case Cons(x, xs) => x * product(xs)
    }

    /**
     * Exercise 3.2
     *
     * Implement the function tail for removing the first element of a List. Note that the
     * function takes constant time. What are different choices you could make in your
     * implementation if the List is Nil? We’ll return to this question in the next
     * chapter.
     */
    def tail[A](as: List[A]): List[A] = as match {
      case Nil => throw new UnsupportedOperationException("tail of empty list")
      case Cons(_, xs) => xs
    }

    /**
     * Exercise 3.3
     *
     * Using the same idea, implement the function setHead for replacing the first element
     * of a List with a different value.
     */
    def setHead[A](as: List[A], a: A): List[A] = as match {
      case Nil => throw new UnsupportedOperationException("setHead of empty list")
      case Cons(_, xs) => Cons(a, xs)
    }

    /**
     * Exercise 3.4
     *
     * Generalize tail to the function drop, which removes the first n elements from a
     * list. Note that this function takes time proportional only to the number of
     * elements being dropped—we don’t need to make a copy of the entire List.
     */
    @tailrec
    def drop[A](as: List[A], n: Int): List[A] = as match {
      case Cons(_, xs) if n > 0 => drop(xs, n - 1)
      case _ => as
    }

    /**
     * Exercise 3.5
     *
     * Implement dropWhile, which removes elements from the List prefix as long as they
     * match a predicate.
     */
    @tailrec
    def dropWhile[A](as: List[A], p: A => Boolean): List[A] = as match {
      case Cons(x, xs) if p(x) => dropWhile(xs, p)
      case _ => as
    }

    /**
     * Exercise 3.6
     *
     * Implement a function, init, that returns a List consisting of all but the last
     * element of a List. So, given List(1,2,3,4), init will return List(1,2,3). Why can’t
     * this function be implemented in constant time like tail?
     */
    def init[A](as: List[A]): List[A] = as match {
      case Nil => throw new UnsupportedOperationException("init of empty list")
      case Cons(_, Nil) => Nil
      case Cons(x, xs) => Cons(x, init(xs))
    }

    /**
     * Exercise 3.7
     *
     * Can product, implemented using foldRight, immediately halt the recursion and return
     * 0.0 if it encounters a 0.0? Why or why not? Consider how any short-circuiting might
     * work if you call foldRight with a large list. This is a deeper question that we’ll
     * return to in chapter 5.
     *
     * Answer: no, because there's no predicate/condition in foldRight, it simply
     * recursively applies a function.
     */

    /**
     * Exercise 3.8
     *
     * See what happens when you pass Nil and Cons themselves to foldRight, like this:
     * foldRight(List(1,2,3), Nil:List[Int])(Cons(_,_)). What do you think this says
     * about the relationship between foldRight and the data constructors of List?
     *
     * Answer: it produces the same list. It says that list apply is a foldRight
     */

    /**
     * Exercise 3.9
     *
     * Compute the length of a list using foldRight
     */
    /** provided */
    def foldRight[A,B](as: List[A], z: B)(f: (A, B) => B): B = as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

    /** challenge */
    def lengthFunc[A](as: List[A]): Int = foldRight(as, 0)((_, y) => 1 + y)

    /**
     * Exercise 3.10
     *
     * Our implementation of foldRight is not tail-recursive and will result in a
     * StackOverflowError for large lists (we say it’s not stack-safe). Convince yourself
     * that this is the case, and then write another general list-recursion function,
     * foldLeft, that is tail-recursive, using the techniques we discussed in the previous
     * chapter.
     */
    @tailrec
    def foldLeft[A,B](as: List[A], z: B)(f: (B, A) => B): B = as match {
      case Nil => z
      case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
    }

    /**
     * Exercise 3.11
     *
     * Write sum, product, and a function to compute the length of a list using foldLeft.
     */
    def sumFL(as: List[Int]): Int = foldLeft(as, 0)(_ + _)
    def productFL(as: List[Int]): Int = foldLeft(as, 1)(_ * _)
    def lengthFL[A](as: List[A]): Int = foldLeft(as, 0)((x, _) => x + 1)

    /**
     * Exercise 3.12
     *
     * Write a function that returns the reverse of a list (given List(1,2,3) it returns
     * List(3,2,1)). See if you can write it using a fold.
     */
    def reverse[A](as: List[A]): List[A] =
      foldLeft(as, Nil: List[A])((acc, currentHead) => Cons(currentHead, acc))

    /**
     * Stepping through:
     *
     * val func = (acc: List[A], currentHead: A) => Cons(currentHead, acc)
     *
     * Step 0: (initial call)
     *   foldLeft(
     *     Cons(10, Cons(11, Cons(2, Nil))),
     *     Nil
     *   )(func)
     *
     * Step 1: (match on first arg)
     *   case Cons(10, Cons(11, Cons(2, Nil))) =>
     *     foldLeft(
     *       Cons(11, Cons(2, Nil)),
     *       Cons(10, Nil)
     *     )(func)
     *
     * Step 2: (match on first arg)
     *   case Cons(11, Cons(2, Nil)) =>
     *     foldLeft(
     *       Cons(2, Nil),
     *       Cons(11, Cons(10, Nil))
     *     )(func)
     *
     * Step 3: (match on first arg)
     *   case Cons(2, Nil) =>
     *     foldLeft(
     *       Nil,
     *       Cons(2, Cons(11, Cons(10, Nil)))
     *     )(func)
     *
     * Step 4: (match on first arg)
     *   case Nil =>
     *     Cons(2, Cons(11, Cons(10, Nil)))
     */

    /**
     * Exercise 3.13
     *
     * Hard: Can you write foldLeft in terms of foldRight? How about the other way around?
     * Implementing foldRight via foldLeft is useful because it lets us implement
     * foldRight tail-recursively, which means it works even for large lists without
     * overflowing the stack.
     */
    def foldLeftInFR[A,B](as: List[A], z: B)(f: (B, A) => B): B =
      foldRight(reverse(as), z)((a: A, b: B) => f(b, a))

    def foldRightInFL[A,B](as: List[A], z: B)(f: (A, B) => B): B =
      foldLeft(reverse(as), z)((b: B, a: A) => f(a, b))

    /**
     * Exercise 3.14
     *
     * Implement append in terms of either foldLeft or foldRight.
     */
    def appendElement[A](as: List[A], a: A): List[A] =
      foldRight(as, Cons(a, Nil))(Cons(_, _))

    def appendList[A](as1: List[A], as2: List[A]): List[A] =
      foldRight(as1, as2)(Cons(_, _))

    /**
     * Exercise 3.15
     *
     * Hard: Write a function that concatenates a list of lists into a single list. Its
     * runtime should be linear in the total length of all lists. Try to use functions we
     * have already defined.
     */
    def flatten[A](ass: List[List[A]]): List[A] =
      foldRight(ass, Nil: List[A])(appendList)

    /**
     * Exercise 3.16
     *
     * Write a function that transforms a list of integers by adding 1 to each element.
     */
    def addOne(is: List[Int]): List[Int] =
      foldRight(is, Nil: List[Int])((x, xs) => Cons(x + 1, xs))

    /**
     * Exercise 3.17
     *
     * Write a function that turns each value in a List[Double] into a String. You can use
     * the expression d.toString to convert some d: Double to a String.
     */
    def mapDoublesToStrings(ds: List[Double]): List[String] =
      foldRight(ds, Nil: List[String])((x, xs) => Cons(x.toString, xs))

    /**
     * Exercise 3.18
     *
     * Write a function map that generalizes modifying each element in a list while
     * maintaining the structure of the list.
     */
    def map[A,B](as: List[A])(f: A => B): List[B] =
      foldRight(as, Nil: List[B])((x, xs) => Cons(f(x), xs))

    /**
     * Exercise 3.19
     *
     * Write a function filter that removes elements from a list unless they satisfy a
     * given predicate. Use it to remove all odd numbers from a List[Int].
     */
    def filter[A](as: List[A])(f: A => Boolean): List[A] = as match {
      case Nil => Nil
      case Cons(x, xs) if f(x) => Cons(x, filter(xs)(f))
      case Cons(y, ys) => filter(ys)(f)
    }

    /**
     * Exercise 3.20
     *
     * Write a function flatMap that works like map except that the function given will
     * return a list instead of a single result, and that list should be inserted into
     * the final resulting list.
     */
    def flatMap[A,B](as: List[A])(f: A => List[B]): List[B] =
      flatten(map(as)(f))

    /**
     * Exercise 3.21
     *
     * Use flatMap to implement filter.
     */
    def filterFM[A](as: List[A])(f: A => Boolean): List[A] =
      flatMap(as){ (a) =>
        if (f(a)) List(a)
        else Nil
      }

    /**
     * Exercise 3.22
     *
     * Write a function that accepts two lists and constructs a new list by adding
     * corresponding elements. For example, List(1,2,3) and List(4,5,6) become
     * List(5,7,9).
     */
    def addElements(xs: List[Int], ys: List[Int]): List[Int] = (xs, ys) match {
      case (x, y) if x == Nil || y == Nil => Nil
      case (Cons(x, xt), Cons(y, yt)) => Cons(x + y, addElements(xt, yt))
    }

    /**
     * Exercise 3.23
     *
     * Generalize the function you just wrote so that it’s not specific to integers or
     * addition. Name your generalized function zipWith.
     */
    def zipWith[A,B,C](as: List[A], bs: List[B])(f: (A, B) => C): List[C] = (as, bs) match {
      case (x, y) if x == Nil || y == Nil => Nil
      case (Cons(x, xs), Cons(y, ys)) => Cons(f(x, y), zipWith(xs, ys)(f))
    }

    /**
     * Exercise 3.24
     *
     * Hard: As an example, implement hasSubsequence for checking whether a List contains
     * another List as a subsequence. For instance, List(1,2,3,4) would have List(1,2),
     * List(2,3), and List(4) as subsequences, among others. You may have some difficulty
     * finding a concise purely functional implementation that is also efficient. That’s
     * okay. Implement the function however comes most naturally. We’ll return to this
     * implementation in chapter 5 and hopefully improve on it. Note: Any two values x and
     * y can be compared for equality in Scala using the expression x == y.
     */
    def take[A](as: List[A], n: Int): List[A] = as match {
      case Cons(x, xs) if n > 0 => Cons(x, take(xs, n - 1))
      case _ => Nil
    }

    def startsWith[A](xs: List[A], ys: List[A]): Boolean =
      take(xs, lengthFL(ys)) == ys

    @tailrec
    def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean =
      if (sup == sub || sub == Nil) true
      else {
        sup match {
          case Nil => false
          case xs if startsWith(xs, sub) => true
          case Cons(x, xs) => hasSubsequence(xs, sub)
        }
      }
  }
}
