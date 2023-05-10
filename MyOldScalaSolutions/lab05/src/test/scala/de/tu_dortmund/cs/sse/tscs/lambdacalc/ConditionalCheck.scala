package de.tu_dortmund.cs.sse.tscs.lambdacalc

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaCalculusSyntax
import de.tu_dortmund.cs.sse.tscs.{FunctionType, TypeInformations}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success

class ConditionalCheck extends AnyFlatSpec with Matchers {
  "if true then false else true" should "parse" in {
    var parserResult = new TypedLambdaCalculusSyntax("if true then false else true").Term.run()
    parserResult shouldBe a[Success[_]]
  }

  "if true then false else true" should "typecheck to boolean" in {
    var parserResult = new TypedLambdaCalculusSyntax("if true then false else true").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldBe a[Success[_]]
    parserResult.get.typecheck().get shouldBe TypeInformations.Bool
  }

  "if if true then false else true then false else true" should "typecheck to boolean" in {
    var parserResult = new TypedLambdaCalculusSyntax("if if true then false else true then false else true").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldBe a[Success[_]]
    parserResult.get.typecheck().get shouldBe TypeInformations.Bool
  }

  "if true then false else true" should "evaluate to false" in {
    var parserResult = new TypedLambdaCalculusSyntax("if true then false else true").Term.run()
    parserResult shouldBe a[Success[_]]
    assertResult ("false") { parserResult.get.-->*.toString() }
  }

  "if true then false else λx.x" should "parse" in {
    var parserResult = new TypedLambdaCalculusSyntax("if true then false else λx.x").Term.run()
    parserResult shouldBe a[Success[_]]
  }


  "if true then λz.z else λx.x" should "typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("if true then λz.z else λx.x").Term.run()
    parserResult shouldBe a[Success[_]]
    val typeInfo = parserResult.get.typecheck()
    typeInfo shouldBe a[Success[_]]
    typeInfo.get shouldEqual FunctionType(TypeInformations.AbstractBaseType, TypeInformations.AbstractBaseType)
  }

  "λz : Bool.if z then true else false" should "typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("λz : Bool.if z then true else false").Term.run()
    parserResult shouldBe a[Success[_]]
    val typeInfo = parserResult.get.typecheck()
    typeInfo shouldBe a[Success[_]]
    typeInfo.get shouldEqual FunctionType(TypeInformations.Bool, TypeInformations.Bool)
  }

  "(λz : Bool.if z then false else true false)" should "typecheck and evaluate to false" in {
    var parserResult = new TypedLambdaCalculusSyntax("(λz : Bool.if z then false else true false)").Term.run()
    parserResult shouldBe a[Success[_]]
    val typeInfo = parserResult.get.typecheck()
    typeInfo shouldBe a[Success[_]]
    typeInfo.get shouldEqual TypeInformations.Bool
    assertResult("true") { parserResult.get.-->*().toString() }
  }

}
