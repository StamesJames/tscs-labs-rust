package de.tu_dortmund.cs.sse.tscs.numexpr

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

/**
  * Test for the N language
  */
class NumericExpressionEvaluationCheck extends AnyFlatSpec with Matchers {
  "Term true" should "evaluate to true" in {
    val parseResult = new NumericExpressionSyntax("true").Term.run()
    parseResult shouldBe a [Success[_]]
    assertResult ("true") { parseResult.get.-->*.toString() }
  }

  "Term false" should "evaluate to false" in {
    val parseResult = new NumericExpressionSyntax("false").Term.run()
    parseResult shouldBe a [Success[_]]
    assertResult ("false") { parseResult.get.-->*.toString() }
  }

  "If expressions"  should "evaluate to false" in {
    val parseResult = new NumericExpressionSyntax("if true then false else true").Term.run()
    parseResult shouldBe a [Success[_]]
    assertResult ("false") { parseResult.get.-->*.toString() }
  }

  "Nested if-expressions"  should "evaluate in one step to if false then false else true" in {
    val parseResult = new NumericExpressionSyntax("if if true then false else true then false else true").Term.run()
    parseResult shouldBe a [Success[_]]
    assertResult (new IfExpr(new ValueExpr("false"), new ValueExpr("false"), new ValueExpr("true"))) { parseResult.get.--> }
  }

  it should "evaluate to true" in {
    val parseResult = new NumericExpressionSyntax("if if true then false else true then false else true").Term.run()
    parseResult shouldBe a [Success[_]]
    assertResult ("true") { parseResult.get.-->*.toString() }
  }

  "Term 0" should "be valid and evaluate to itself" in {
    val parseResult = new NumericExpressionSyntax("0").Term.run()
    parseResult shouldBe a [Success[_]]
    parseResult.get.-->*().toString() shouldBe "0"
  }

  "Term succ succ 0" should "be valid and evaluate to itself" in {
    val parseResult = new NumericExpressionSyntax("succ succ 0").Term.run()
    parseResult shouldBe a [Success[_]]
    parseResult.get.-->*().toString() shouldBe "succ succ 0"
  }

  "Term iszero 0" should "be valid and evaluate to true" in {
    val parseResult = new NumericExpressionSyntax("iszero 0").Term.run()
    parseResult shouldBe a [Success[_]]
    parseResult.get.-->*().toString() shouldBe "true"
  }

  "Term iszero succ 0" should "be valid and evaluate to false" in {
    val parseResult = new NumericExpressionSyntax("iszero succ 0").Term.run()
    parseResult shouldBe a [Success[_]]
    parseResult.get.-->*().toString() shouldBe "false"
  }

  "Term iszero true" should "be valid and get stuck" in {
    val parseResult = new NumericExpressionSyntax("iszero true").Term.run()
    parseResult shouldBe a [Success[_]]
    parseResult.get.-->*() shouldBe null
  }

  "Term succ pred 0" should "evaluate to succ 0" in {
    val parseResult = new NumericExpressionSyntax("succ pred 0").Term.run()
    parseResult shouldBe a [Success[_]]
    parseResult.get.-->*().toString() shouldBe "succ 0"
  }

}
