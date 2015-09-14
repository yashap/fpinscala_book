package com.yasha.fpinscala.chapter03

object Trees {

  /** provided */
  sealed trait Tree[+A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  object Tree {

    /**
     * Exercise 3.25
     *
     * Write a function size that counts the number of nodes (leaves and branches) in a
     * tree.
     */
    def treeSize[A](tr: Tree[A]): Int = tr match {
      case Leaf(_) => 1
      case Branch(l, r) => 1 + treeSize(l) + treeSize(r)
    }

    /**
     * Exercise 3.26
     *
     * Write a function size that counts the number of nodes (leaves and branches) in a
     * tree.
     */
    def maximum(tr: Tree[Int]): Int = tr match {
      case Leaf(v) => v
      case Branch(l, r) => maximum(l) max maximum(r)
    }

    /**
     * Exercise 3.27
     *
     * Write a function depth that returns the maximum path length from the root of a tree
     * to any leaf.
     */
    def depth[A](tr: Tree[A]): Int = tr match {
      case Leaf(_) => 1
      case Branch(l, r) => (1 + depth(l)) max (1 + depth(r))
    }

    /**
     * Exercise 3.28
     *
     * Write a function map, analogous to the method of the same name on List, that
     * modifies each element in a tree with a given function.
     */
    def map[A,B](tr: Tree[A])(f: A => B): Tree[B] = tr match {
      case Leaf(v) => Leaf(f(v))
      case Branch(l, r) => Branch(map(l)(f), map(r)(f))
    }

    /**
     * Exercise 3.29
     *
     * Generalize size, maximum, depth, and map, writing a new function fold that
     * abstracts over their similarities. Reimplement them in terms of this more general
     * function. Can you draw an analogy between this fold function and the left and right
     * folds for List?
     */
    def fold[A,B](tr: Tree[A])(leafFunc: A => B)(combineFunc: (B, B) => B): B = tr match {
      case Leaf(v) => leafFunc(v)
      case Branch(l, r) => combineFunc(fold(l)(leafFunc)(combineFunc), fold(r)(leafFunc)(combineFunc))
    }

    def treeSizeViaFold[A](tr: Tree[A]): Int =
      fold(tr)((v) => 1)((l, r) => 1 + l + r)

    def maximumViaFold(tr: Tree[Int]): Int =
      fold(tr)(identity)((l, r) => l max r)

    def depthViaFold[A](tr: Tree[A]): Int =
      fold(tr)((v) => 1)((l, r) => (1 + l) max (1 + r))

    def mapViaFold[A,B](tr: Tree[A])(f: A => B): Tree[B] =
      fold(tr)((v) => Leaf(f(v)): Tree[B])((l, r) => Branch(l, r))

  }
}
