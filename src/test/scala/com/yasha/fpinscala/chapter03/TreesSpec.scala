package com.yasha.fpinscala.chapter03

import com.yasha.fpinscala.UnitSpec

class TreesSpec extends UnitSpec {
  import Trees._
  import Tree._

  val intTree: Tree[Int] = Branch(Branch(Branch(Leaf(10), Leaf(11)), Leaf(12)), Branch(Leaf(13), Leaf(14)))
  val intTree2: Tree[Int] = Branch(Branch(Branch(Leaf(14), Leaf(13)), Leaf(12)), Branch(Leaf(11), Leaf(10)))
  val intTree3: Tree[Int] = Branch(Leaf(5), Leaf(5))

  "size" should "count all nodes (leaves and branches) in a tree" in {
    treeSize(intTree) shouldBe 9
    treeSize(intTree2) shouldBe 9
    treeSize(intTree3) shouldBe 3
  }

  "maximum" should "find the max value of any node in a tree of integers" in {
    maximum(intTree) shouldBe 14
    maximum(intTree2) shouldBe 14
    maximum(intTree3) shouldBe 5
  }

  "depth" should "find the maximum path length from the root of a tree to any leaf" in {
    depth(intTree) shouldBe 4
    depth(intTree2) shouldBe 4
    depth(intTree3) shouldBe 2
  }

  "map" should "apply a function to every leaf" in {
    val f: Int => Int = (v) => v * 10

    map(intTree)(f) shouldBe Branch(Branch(Branch(Leaf(100), Leaf(110)), Leaf(120)), Branch(Leaf(130), Leaf(140)))
    map(intTree2)(f) shouldBe Branch(Branch(Branch(Leaf(140), Leaf(130)), Leaf(120)), Branch(Leaf(110), Leaf(100)))
    map(intTree3)(f) shouldBe Branch(Leaf(50), Leaf(50))
  }

  "treeSizeViaFold" should "work the same as the previous treeSize implementation" in {
    treeSizeViaFold(intTree) shouldBe treeSize(intTree)
    treeSizeViaFold(intTree2) shouldBe treeSize(intTree2)
    treeSizeViaFold(intTree3) shouldBe treeSize(intTree3)
  }

  "maximumViaFold" should "work the same as the previous maximum implementation" in {
    maximumViaFold(intTree) shouldBe maximum(intTree)
    maximumViaFold(intTree2) shouldBe maximum(intTree2)
    maximumViaFold(intTree3) shouldBe maximum(intTree3)
  }

  "depthViaFold" should "work the same as the previous depth implementation" in {
    depthViaFold(intTree) shouldBe depth(intTree)
    depthViaFold(intTree2) shouldBe depth(intTree2)
    depthViaFold(intTree3) shouldBe depth(intTree3)
  }

  "mapViaFold" should "work the same as the previous map implementation" in {
    val f: Int => Int = (v) => v * 10

    mapViaFold(intTree)(f) shouldBe map(intTree)(f)
    mapViaFold(intTree2)(f) shouldBe map(intTree2)(f)
    mapViaFold(intTree3)(f) shouldBe map(intTree3)(f)
  }
}
