package de.tu_dortmund.cs.sse.tscs.numexpr

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

/**
  * Test for the N language
  */
class NumericExpressionSyntaxCheck extends AnyFlatSpec with Matchers {
  "Term true" should "be valid" in {
    val parseResult = new NumericExpressionSyntax("true").InputLine.run()
    parseResult shouldBe a [Success[_]]
  }

  "Term false" should "be valid" in {
    val parseResult = new NumericExpressionSyntax("false").InputLine.run()
    parseResult shouldBe a [Success[_]]
  }

  "Term x" should "not be valid" in {
    val parseResult = new NumericExpressionSyntax("x").InputLine.run()
    parseResult shouldBe a [Failure[_]]
  }

  "If expressions" should "be valid" in {
    val parseResult = new NumericExpressionSyntax("if true then false else true").InputLine.run()
    parseResult shouldBe a [Success[_]]
  }

  "Nested if-expressions" should "be valid" in {
    val parseResult = new NumericExpressionSyntax("if if true then false else true then false else true").InputLine.run()
    parseResult shouldBe a [Success[_]]
  }

  "Term 0" should "be valid" in {
    val parseResult = new NumericExpressionSyntax("0").Term.run()
    parseResult shouldBe a [Success[_]]
  }

  "Term 1" should "not be valid" in {
    val parseResult = new NumericExpressionSyntax("1").Term.run()
    parseResult shouldBe a [Failure[_]]
  }

  "Term succ succ 0" should "be valid" in {
    val parseResult = new NumericExpressionSyntax("succ succ 0").Term.run()
    parseResult shouldBe a [Success[_]]
  }

  "Term iszero 0" should "be valid" in {
    val parseResult = new NumericExpressionSyntax("iszero 0").Term.run()
    parseResult shouldBe a [Success[_]]
  }

  "Term iszero succ 0" should "be valid" in {
    val parseResult = new NumericExpressionSyntax("iszero succ 0").Term.run()
    parseResult shouldBe a [Success[_]]
  }

  "Term iszero true" should "be valid" in {
    val parseResult = new NumericExpressionSyntax("iszero true").Term.run()
    parseResult shouldBe a [Success[_]]
  }

  "Term succ pred 0" should "be valid" in {
    val parseResult = new NumericExpressionSyntax("succ pred 0").Term.run()
    parseResult shouldBe a [Success[_]]
  }

}
