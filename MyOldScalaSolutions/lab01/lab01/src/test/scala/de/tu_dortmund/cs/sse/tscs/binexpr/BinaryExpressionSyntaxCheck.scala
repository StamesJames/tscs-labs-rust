package de.tu_dortmund.cs.sse.tscs.binexpr

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

/**
  * Test for the B language
  */
class BinaryExpressionSyntaxCheck extends AnyFlatSpec with Matchers {

  "Term true" should "be valid" in {
    val parseResult = new BinaryExpressionSyntax("true").InputLine.run()
    parseResult shouldBe a [Success[_]]
  }

  "Term false" should "be valid" in {
    val parseResult = new BinaryExpressionSyntax("false").InputLine.run()
    parseResult shouldBe a [Success[_]]
  }

  "Term x" should "not be valid" in {
    val parseResult = new BinaryExpressionSyntax("x").InputLine.run()
    parseResult shouldBe a [Failure[_]]
  }

  "If expressions" should "be valid" in {
    val parseResult = new BinaryExpressionSyntax("if true then false else true").InputLine.run()
    parseResult shouldBe a [Success[_]]
  }

  "Nested if-expressions" should "be valid" in {
    val parseResult = new BinaryExpressionSyntax("if if true then false else true then false else true").InputLine.run()
    parseResult shouldBe a [Success[_]]
  }

}
