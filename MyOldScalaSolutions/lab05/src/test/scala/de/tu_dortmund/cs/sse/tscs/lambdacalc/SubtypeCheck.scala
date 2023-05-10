package de.tu_dortmund.cs.sse.tscs.lambdacalc

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional.{BooleanConstant, IfExpr}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records.{Record, RecordType}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references.{Dereferencing, ReferenceType}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.{TypedLambdaCalculusSyntax, TypedLambdaVariable}
import de.tu_dortmund.cs.sse.tscs.{FunctionType, TypeInformations}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

class SubtypeCheck extends AnyFlatSpec with Matchers {
  "An application with incompatible base types" should "fail" in {
    def parse(program: String) = new TypedLambdaCalculusSyntax(program).Term.run()

    val incompatibleBaseTypes = parse("(λx : A.x true))")
    incompatibleBaseTypes.get.typecheck() shouldBe a [Failure[_]]
  }

  "An application of a more detailed record to a function with a lesser detailed record as a parameter" should "typecheck" in {
    val program = "(λx : {test=Bool}.<x.test> {test=false,another=true})"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get.typecheck() shouldEqual Success(TypeInformations.Bool)
  }


  "An application of a record to a general identity function" should "typecheck" in {
    val program = "(λx : Top.x {test=false,another=true})"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get.typecheck() shouldEqual Success(TypeInformations.Top)
  }


  "An if expression with two different records" should "typecheck to the join of these types" in {
    val program = "if true then {test=true,another=false} else {test=false,basic=true}"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult shouldEqual Success(
      IfExpr(
        BooleanConstant("true"),
        Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false"))),
        Record(Map("test" -> BooleanConstant("false"), "basic" -> BooleanConstant("true")))
      )
    )
    parserResult.get.typecheck() shouldEqual Success(RecordType(Map("test" -> TypeInformations.Bool)))
  }

  "A record with a complex type" should "typecheck" in {
    val program = "{test=λx : Top.x,another=λy : Ref Bool.<!y>}"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult shouldEqual Success(
        Record(Map("test" ->
          LambdaAbstraction(
            TypedLambdaVariable("x", Some(TypeInformations.Top)),
            TypedLambdaVariable("x")),
          "another" ->
            LambdaAbstraction(
              TypedLambdaVariable("y", Some(ReferenceType(TypeInformations.Bool))),
              Dereferencing(TypedLambdaVariable("y")))))
    )
    parserResult.get.typecheck() shouldEqual Success(RecordType(Map(
      "test" -> FunctionType(TypeInformations.Top, TypeInformations.Top),
      "another" -> FunctionType(ReferenceType(TypeInformations.Bool), TypeInformations.Bool))))
  }

  "An if expression with two different complex records" should "typecheck to the join of these types" in {
    val program = "if true then {test=λx : Top.x,another=λy : Ref Bool.<!y>} else {test=λx : Top.x,another=λy : Ref A.<!y>}"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult shouldEqual Success(
      IfExpr(
        BooleanConstant("true"),
        Record(Map("test" ->
          LambdaAbstraction(
            TypedLambdaVariable("x", Some(TypeInformations.Top)),
            TypedLambdaVariable("x")),
          "another" ->
            LambdaAbstraction(
              TypedLambdaVariable("y", Some(ReferenceType(TypeInformations.Bool))),
              Dereferencing(TypedLambdaVariable("y"))))),
        Record(Map("test" ->
          LambdaAbstraction(
            TypedLambdaVariable("x", Some(TypeInformations.Top)),
            TypedLambdaVariable("x")),
          "another" ->
            LambdaAbstraction(
              TypedLambdaVariable("y", Some(ReferenceType(TypeInformations.AbstractBaseType))),
              Dereferencing(TypedLambdaVariable("y")))))
      )
    )
    parserResult.get.typecheck() shouldEqual Success(RecordType(Map("test" ->  FunctionType(TypeInformations.Top, TypeInformations.Top))))
  }

  "if true then false else λx.x" should "typecheck to Top" in {
    new TypedLambdaCalculusSyntax("true").Term.run().get.typecheck().get shouldEqual TypeInformations.Bool

    var parserResult = new TypedLambdaCalculusSyntax("if true then false else λx.x").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldEqual Success(TypeInformations.Top)
  }

  "if true then (λx : Bool.x true) else x" should "not typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("if true then (λx : Bool.x true) else x").Term.run()
    parserResult shouldBe a[Success[_]]
    val typeInfo = parserResult.get.typecheck()
    typeInfo shouldEqual Success(TypeInformations.Top)
  }
}
