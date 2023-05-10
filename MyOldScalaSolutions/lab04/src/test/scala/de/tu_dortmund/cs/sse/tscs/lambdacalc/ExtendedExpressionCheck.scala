package de.tu_dortmund.cs.sse.tscs.lambdacalc

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.{TypedLambdaCalculusSyntax, TypedLambdaVariable}
import de.tu_dortmund.cs.sse.tscs.{FunctionType, TypeInformations}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional.BooleanConstant
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.let.Let
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references.{Allocation, Assignment, Dereferencing, ReferenceType}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing.Sequence
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit.{UnitConstant, UnitType}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

/**
  * Checks for the type checker
  */
class ExtendedExpressionCheck extends AnyFlatSpec with Matchers {

  /* Check for unit typing */
  "unit" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("unit").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(UnitConstant)
    parserResult.get.typecheck() shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldEqual Success(UnitType)
  }

  "λx : Unit.x" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("λx : Unit.x").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(LambdaAbstraction(TypedLambdaVariable("x", Some(UnitType)), TypedLambdaVariable("x", None)))
    parserResult.get.typecheck() shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldEqual Success(FunctionType(UnitType, UnitType))
  }

  /* Check sequence evaluation */
  "{unit;λx : Unit.x}" should "successfully evaluate to unit identity function" in {
    var parserResult = new TypedLambdaCalculusSyntax("{unit;λx : Unit.x}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Sequence(UnitConstant, LambdaAbstraction(TypedLambdaVariable("x", Some(UnitType)), TypedLambdaVariable("x", None))))
    parserResult.get.-->*() shouldEqual LambdaAbstraction(TypedLambdaVariable("x", Some(UnitType)), TypedLambdaVariable("x", None))
  }

  /* Check sequence typing */
  "{unit;λx : Unit.x}" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("{unit;λx : Unit.x}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Sequence(UnitConstant, LambdaAbstraction(TypedLambdaVariable("x", Some(UnitType)), TypedLambdaVariable("x", None))))
    parserResult.get.typecheck() shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldEqual Success(FunctionType(UnitType, UnitType))
  }

  "{unit;{unit;λx : Unit.x}}" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("{unit;{unit;λx : Unit.x}}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Sequence(UnitConstant, Sequence(UnitConstant, LambdaAbstraction(TypedLambdaVariable("x", Some(UnitType)), TypedLambdaVariable("x", None)))))
    parserResult.get.typecheck() shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldEqual Success(FunctionType(UnitType, UnitType))
  }

  // Relaxing the sequence type check a bit
  "{unit;{λx : Unit.x;λx : Unit.x}}" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("{unit;{λx : Unit.x;λx : Unit.x}}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Sequence(UnitConstant, Sequence(LambdaAbstraction(TypedLambdaVariable("x", Some(UnitType)), TypedLambdaVariable("x", None)), LambdaAbstraction(TypedLambdaVariable("x", Some(UnitType)), TypedLambdaVariable("x", None)))))
    parserResult.get.typecheck() shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldEqual Success(FunctionType(UnitType, UnitType))
  }

  /* Let binding evaluation */
  "let x=true in x" should "evaluation to true" in {
    var parserResult = new TypedLambdaCalculusSyntax("let x=true in x").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Let(TypedLambdaVariable("x", None), BooleanConstant("true", None), TypedLambdaVariable("x", None)))
    parserResult.get.-->*() shouldEqual BooleanConstant("true", None)
  }

  /* Let binding typecheck */
  "let x=false in x" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("let x=false in x").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Let(TypedLambdaVariable("x", None), BooleanConstant("false", None), TypedLambdaVariable("x", None)))
    parserResult.get.typecheck() shouldBe a[Success[_]]
  }

  /* Reference typechecks */
  "λx : Unit.<!x>" should "not successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("λx : Unit.<!x>").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(LambdaAbstraction(TypedLambdaVariable("x", Some(UnitType)), Dereferencing(TypedLambdaVariable("x", None))))
    parserResult.get.typecheck() shouldBe a[Failure[_]]

    var shouldNotFail = new TypedLambdaCalculusSyntax("λx : Ref Bool.<!x>").Term.run()
    shouldNotFail shouldBe a [Success[_]]
    shouldNotFail.get.typecheck() shouldBe a [Success[_]]
  }

  "λx : Ref Bool.<!x>" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("λx : Ref Bool.<!x>").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(LambdaAbstraction(TypedLambdaVariable("x", Some(ReferenceType(TypeInformations.Bool))), Dereferencing(TypedLambdaVariable("x", None))))
    parserResult.get.typecheck() shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldEqual Success(FunctionType(ReferenceType(TypeInformations.Bool), TypeInformations.Bool))
  }

  "<ref true>" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("<ref true>").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Allocation(BooleanConstant("true", None)))
    parserResult.get.typecheck() shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldEqual Success(ReferenceType(TypeInformations.Bool))
  }


  "let x=<ref true> in {<x:=false>;<!x>}" should "evaluate to false" in {
    var parserResult = new TypedLambdaCalculusSyntax("let x=<ref true> in {<x:=false>;<!x>}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(
      Let(
        TypedLambdaVariable("x", None),
        Allocation(
          BooleanConstant("true", None)),
        Sequence(
          Assignment(
            TypedLambdaVariable("x", None),
            BooleanConstant("false", None)),
          Dereferencing(TypedLambdaVariable("x", None)))))
    val step = parserResult.get.runFull()
    step should not be (null)
    step._1 shouldEqual BooleanConstant("false", None)
  }



  "let x=<ref true> in {<x:=false>;<!x>}" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("let x=<ref true> in {<x:=false>;<!x>}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(
      Let(
        TypedLambdaVariable("x", None),
        Allocation(
          BooleanConstant("true", None)),
        Sequence(
          Assignment(
            TypedLambdaVariable("x", None),
            BooleanConstant("false", None)),
          Dereferencing(TypedLambdaVariable("x", None)))))
    parserResult.get.typecheck() shouldBe a[Success[_]]
  }


  "λx : Ref Bool.<x:=(λy : Bool.if y then false else true <!x>)>;<!x>" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("λx : Ref Bool.<x:=(λy : Bool.if y then false else true <!x>)>;<!x>").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldBe a[Success[_]]
  }


}
