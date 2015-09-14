package com.yasha.fpinscala.laziness

trait Stream[+A] {
  import Stream._

  def foldRight[B](z: => B)(f: (A, => B) => B): B =
  // The arrow `=>` in front of the argument type `B` means that the function `f`
  // takes its second argument by name and may choose not to evaluate it.
    this match {
      case Cons(h,t) => f(h(), t().foldRight(z)(f))
      // If `f` doesn't evaluate its second argument, the recursion never occurs.
      case _ => z
    }

  def exists(p: A => Boolean): Boolean =
    foldRight(false)((a, b) => p(a) || b)
  // Here `b` is the unevaluated recursive step that folds the tail of the
  // stream. If `p(a)` returns `true`, `b` will never be evaluated and the
  // computation terminates early.

  @annotation.tailrec
  final def find(f: A => Boolean): Option[A] = this match {
    case Empty => None
    case Cons(h, t) => if (f(h())) Some(h()) else t().find(f)
  }

  def toList: List[A] =
    this.foldRight(Nil: List[A])(_ :: _)

  def headOption: Option[A] =
    this match {
      case Empty => None
      case Cons(h, t) => Some(h())
      // Note that we must force h explicitly with h()
      // Also not that we didn't have to evaluate the tail!
    }

  def take(n: Int): Stream[A] =
    this match {
      case Cons(h, t) if n > 1 => cons(h(), t().take(n - 1))
      case Cons(h, t) if n == 1 => cons(h(), empty)
      case _ => empty
    }
    // More elegant, but less efficient, because the above doesn't look at tail for n == 1:
//    this match {
//      case Cons(h, t) if n > 0 => cons(h(), t().take(n - 1))
//      case _ => empty
//    }

  @annotation.tailrec
  final def drop(n: Int): Stream[A] =
    this match {
      case Cons(_, t) if n > 0 => t().drop(n - 1)
      case _ => this
    }

  def takeWhile(p: A => Boolean): Stream[A] =
    this match {
      case Cons(h, t) if p(h()) => cons(h(), t().takeWhile(p))
      case _ => Empty
    }

  def forAll(p: A => Boolean): Boolean =
    foldRight(true)((a, b) => p(a) && b)

  def takeWhileFR(p: A => Boolean): Stream[A] =
    foldRight(empty[A])((a, b) =>
      if (p(a)) cons(a, b)
      else empty
    )

  def headOptionFR: Option[A] =
    foldRight(None: Option[A])((a, _) => Some(a))
    // This works because if foldRight works on something that's not a Cons(h,t)
    // then it just returns the zero value (None)

  def map[B](f: A => B): Stream[B] =
    foldRight(empty[B])((h, t) => cons(f(h), t))

  def filter(p: A => Boolean): Stream[A] =
    foldRight(empty[A])((h, t) =>
      if (p(h)) cons(h, t)
      else t
    )

  def append[B>:A](a: => Stream[B]): Stream[B] =
    foldRight(a)((h, t) => cons(h, t))

//  def append(a: => Stream[A]): Stream[A] =
//    foldRight(a)((h, t) => cons(h, t))
//  TODO: look into covariance, figure out why this doesn't work

  def flatMap[B](f: A => Stream[B]): Stream[B] =
    foldRight(empty[B])((h, t) => f(h) append t)

  def mapU[B](f: A => B): Stream[B] =
    unfold(this){
      case Cons(h, t) => Some((f(h()), t()))
      case _ => None
    }

  def takeU(n: Int): Stream[A] =
    unfold((this, n)){
      case (Cons(h, t), 1) => Some((h(), (empty, 0)))
      case (Cons(h, t), n) if n > 1 => Some((h(), (t(), n - 1)))
      case _ => None
    }

  def takeWhileU(p: A => Boolean): Stream[A] =
    unfold(this){
      case Cons(h, t) if p(h()) => Some((h(), t()))
      case _ => None
    }

  def zipWith[B, C](that: Stream[B])(f: (A, B) => C): Stream[C] =
    unfold((this, that)){
//      case (Empty, _) => None
//      case (_, Empty) => None
      case (Cons(h1, t1), Cons(h2, t2)) => Option((f(h1(), h2()), (t1(), t2())))
      case _ => None
    }

  def zipAllU[B](that: Stream[B]): Stream[(Option[A],Option[B])] =
    zipWithAll(that)((_, _))

  def zipWithAll[B, C](that: Stream[B])(f: (Option[A], Option[B]) => C): Stream[C] =
    unfold((this, that)){
      case (Empty, Empty) => None
      case (Cons(h, t), Empty) => Some(f(Some(h()), Option.empty[B]) -> (t(), empty[B]))
      case (Empty, Cons(h, t)) => Some(f(Option.empty[A], Some(h())) -> (empty[A] -> t()))
      case (Cons(h1, t1), Cons(h2, t2)) => Some(f(Some(h1()), Some(h2())) -> (t1() -> t2()))
    }

  def startsWith[B](that: Stream[B]): Boolean =
    zipAllU(that).foldRight(true){
      (h, t) => h match {
        case (h1, h2) if h1 == h2 => t
        case (_, None) => true
        case _ => false
      }
    }

  def tails: Stream[Stream[A]] =
    unfold(this){
      case Cons(h, t) => Some(cons(h(), t()), t())
      case _ => None
    }.append(Stream(empty))

  def hasSubsequence[A](s: Stream[A]): Boolean =
    this.tails exists (_ startsWith s)

  /**
   * The function can't be implemented using `unfold`, since `unfold` generates elements of the `Stream` from
   * left to right. It can be implemented using `foldRight` though.
   *
   * The implementation is just a `foldRight`
   * that keeps the accumulated value and the stream of intermediate results, which we `cons` onto during each
   * iteration. When writing folds, it's common to have more state in the fold than is needed to compute the result.
   * Here, we simply extract the accumulated list once finished.
   *
   * Note that p0 is passed by-name and used in by-name args in f and cons. So we can use lazy val to ensure only
   * one evaluation...
  */
  def scanRight[B](z: B)(f: (A, => B) => B): Stream[B] =
    this.foldRight((z, Stream(z))){
      (a, p0) => {
        lazy val p1 = p0
        val b2 = f(a, p1._1)
        (b2, cons(b2, p1._2))
      }
    }._2

}

object Stream {

  case object Empty extends Stream[Nothing]
  case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]
  // Basically a list, but both the head and tail are CNB

  // A smart constructor for creating a non-empty stream
  // So, like Cons, but with caching
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  // A smart constructor for creating an empty stream of a specific type
  def empty[A]: Stream[A] = Empty

  // Convenience method for constructing a Stream from multiple args
  // Uses the smart constructor
  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))

  val ones: Stream[Int] =
    cons(1, ones)

  def constant[A](a: A): Stream[A] =
    cons(a, constant(a))

  def from(n: Int): Stream[Int] =
    cons(n, from(n + 1))

  val fibs: Stream[Int] = {
    def loop(a: Int, b: Int): Stream[Int] =
      cons(a, loop(b, a + b))
    loop(0, 1)
  }

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] =
    f(z) match {
      case Some((h, s)) => cons(h, unfold(s)(f))
      case None => empty
    }

  val onesU: Stream[Int] =
    unfold(1)(x => Option((x, x)))

  def constantU[A](a: A): Stream[A] =
    unfold(a)(x => Option((x, x)))

  def fromU(n: Int): Stream[Int] =
    unfold(n)(x => Option((x, x + 1)))

  val fibsU: Stream[Int] = {
    unfold((0, 1)){ case (a, b) => Some((a, (b, a + b))) }
  }

}

// Left off page 76, excercise 5.15
