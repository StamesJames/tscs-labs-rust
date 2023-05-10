package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.TypeInformations
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

class NumericExpressionTypecheck extends AnyFlatSpec with Matchers {
  "true" should "typecheck" in {
    val parseResult = new NumericExpressionSyntax("true").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Success[_]]
    typeCheckResult.get shouldBe TypeInformations.Bool
  }

  "if true then true else false" should "typecheck" in {
    val parseResult = new NumericExpressionSyntax("if true then true else false").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Success[_]]
    typeCheckResult.get shouldBe TypeInformations.Bool
  }

  "if true then 0 else false" should "not typecheck" in {
    val parseResult = new NumericExpressionSyntax("if true then 0 else false").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Failure[_]]
  }

  "Term false" should "typecheck to Bool" in {
    val parseResult = new NumericExpressionSyntax("false").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Success[_]]
    typeCheckResult.get shouldBe TypeInformations.Bool
  }

  "Nested if-expressions" should "typecheck" in {
    val parseResult = new NumericExpressionSyntax("if if true then false else true then false else true").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Success[_]]
  }


  "Term 0" should "typecheck to Nat" in {
    val parseResult = new NumericExpressionSyntax("0").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Success[_]]
    typeCheckResult.get shouldBe TypeInformations.Nat
  }

  "Term succ succ 0" should "typecheck to Nat" in {
    val parseResult = new NumericExpressionSyntax("succ succ 0").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Success[_]]
    typeCheckResult.get shouldBe TypeInformations.Nat
  }

  "Term iszero 0" should "typecheck to Bool" in {
    val parseResult = new NumericExpressionSyntax("iszero 0").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Success[_]]
    typeCheckResult.get shouldBe TypeInformations.Bool
  }

  "Term iszero succ 0" should "typecheck to Bool" in {
    val parseResult = new NumericExpressionSyntax("iszero succ 0").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Success[_]]
    typeCheckResult.get shouldBe TypeInformations.Bool
  }

  "Term iszero true" should "not typecheck" in {
    val parseResult = new NumericExpressionSyntax("iszero true").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Failure[_]]
  }

  "Term succ pred 0" should "typecheck to Nat" in {
    val parseResult = new NumericExpressionSyntax("succ pred 0").Term.run()
    parseResult shouldBe a [Success[_]]
    val typeCheckResult = parseResult.get.typecheck()
    typeCheckResult shouldBe a [Success[_]]
    typeCheckResult.get shouldBe TypeInformations.Nat
  }

}
