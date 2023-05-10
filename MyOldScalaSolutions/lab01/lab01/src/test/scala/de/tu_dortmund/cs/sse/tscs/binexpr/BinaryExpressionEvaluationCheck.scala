package de.tu_dortmund.cs.sse.tscs.binexpr

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success

class BinaryExpressionEvaluationCheck extends AnyFlatSpec with Matchers {

  "Term true" should "evaluate to true" in {
    val parseResult = new BinaryExpressionSyntax("true").Term.run()
    parseResult shouldBe a[Success[_]]
    assertResult("true") {
      parseResult.get.-->*.toString()
    }
  }

  "Term false" should "evaluate to false" in {
    val parseResult = new BinaryExpressionSyntax("false").Term.run()
    parseResult shouldBe a[Success[_]]
    assertResult("false") {
      parseResult.get.-->*.toString()
    }
  }

  "If condition" should "evaluate to false" in {
    val parseResult = new BinaryExpressionSyntax("if true then false else true").Term.run()
    parseResult shouldBe a[Success[_]]
    assertResult("false") {
      parseResult.get.-->*.toString()
    }
  }


  "Nested if condition" should "evaluate in one step to if false then false else true" in {
    val parseResult = new BinaryExpressionSyntax("if if true then false else true then false else true").Term.run()
    parseResult shouldBe a[Success[_]]
    assertResult(new IfExpr(new ValueExpr("false"), new ValueExpr("false"), new ValueExpr("true"))) {
      parseResult.get.-->
    }
  }


  it should "evaluate to true" in {
    val parseResult = new BinaryExpressionSyntax("if if true then false else true then false else true").Term.run()
    parseResult shouldBe a[Success[_]]
    assertResult("true") {
      parseResult.get.-->*.toString()
    }
  }

}
