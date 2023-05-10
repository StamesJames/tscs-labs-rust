package de.tu_dortmund.cs.sse.tscs.lambdacalc

import de.tu_dortmund.cs.sse.tscs.TypeInformations
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional.{BooleanConstant, IfExpr}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records.{Record, RecordProjection, RecordType}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.{TypedLambdaCalculusSyntax, TypedLambdaVariable}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success

class RecordCheck extends AnyFlatSpec with Matchers {
  "An empty record" should "parse to an empty record" in {
    val parserResult = new TypedLambdaCalculusSyntax("{}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map()))
  }

  it should "evaluate to itself in stateless mode" in {
    val parserResult = new TypedLambdaCalculusSyntax("{}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map()))
    parserResult.get.-->*() shouldEqual (Record(Map()))
  }

  it should "typecheck to an empty record type" in {
    val parserResult = new TypedLambdaCalculusSyntax("{}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map()))
    parserResult.get.typecheck() shouldEqual Success(RecordType(Map()))
  }

  it should "evaluate to itself in stateful mode" in {
    val parserResult = new TypedLambdaCalculusSyntax("{}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map()))
    parserResult.get.runFull() shouldEqual((Record(Map())), List())
  }

  "A simple record" should "parse fine" in {
    val parserResult = new TypedLambdaCalculusSyntax("{test=true}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map("test" -> BooleanConstant("true"))))
  }

  it should "evaluate to itself in stateless mode" in {
    val parserResult = new TypedLambdaCalculusSyntax("{test=true}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map("test" -> BooleanConstant("true"))))
    parserResult.get.-->*() shouldEqual (Record(Map("test" -> BooleanConstant("true"))))
  }

  it should "evaluate to itself in stateful mode" in {
    val parserResult = new TypedLambdaCalculusSyntax("{test=true}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map("test" -> BooleanConstant("true"))))
    parserResult.get.runFull() shouldEqual((Record(Map("test" -> BooleanConstant("true")))), List())
  }

  it should "typecheck to a record type including a boolean type" in {
    val parserResult = new TypedLambdaCalculusSyntax("{test=true}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map("test" -> BooleanConstant("true"))))
    parserResult.get.typecheck() shouldEqual Success(RecordType(Map("test" -> TypeInformations.Bool)))
  }

  "A record with two labels" should "parse fine" in {
    val parserResult = new TypedLambdaCalculusSyntax("{test=true,another=false}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false"))))
  }

  it should "evaluate to itself in stateless mode" in {
    val parserResult = new TypedLambdaCalculusSyntax("{test=true,another=false}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false"))))
    parserResult.get.-->*() shouldEqual Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false")))
  }

  it should "evaluate to itself in stateful mode" in {
    val parserResult = new TypedLambdaCalculusSyntax("{test=true,another=false}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false"))))
    parserResult.get.runFull() shouldEqual((Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false")))), List())
  }

  it should "typecheck" in {
    val parserResult = new TypedLambdaCalculusSyntax("{test=true,another=false}").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false"))))
    parserResult.get.typecheck() shouldEqual Success(RecordType(Map("test" -> TypeInformations.Bool, "another" -> TypeInformations.Bool)))
  }

  "A projection on record with two labels" should "parse fine" in {
    val parserResult = new TypedLambdaCalculusSyntax("<{test=true,another=false}.test>").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(RecordProjection(Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false"))), "test"))
  }


  it should "evaluate to the projected field" in {
    val parserResult = new TypedLambdaCalculusSyntax("<{test=true,another=false}.test>").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(RecordProjection(Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false"))), "test"))
    parserResult.get.-->*() shouldEqual BooleanConstant("true")
  }

  it should "typecheck" in {
    val parserResult = new TypedLambdaCalculusSyntax("<{test=true,another=false}.test>").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(RecordProjection(Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false"))), "test"))
    parserResult.get.typecheck() shouldEqual Success(TypeInformations.Bool)
  }

  "A projection on a more complex record" should "parse fine" in {
    val program = "<{test=(λx : Bool.x false),another=if if true then false else true then false else true}.test>"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult shouldEqual Success(RecordProjection(Record(Map(
      "test" ->
        LambdaApplication(
          LambdaAbstraction(
            TypedLambdaVariable("x", Some(TypeInformations.Bool)), TypedLambdaVariable("x")),
          BooleanConstant("false")),
      "another" ->
        IfExpr(
          IfExpr(BooleanConstant("true"),BooleanConstant("false"),BooleanConstant("true")),
            BooleanConstant("false"),
            BooleanConstant("true"))
        )), "test"))
  }

  it should "evaluate to the evaluated version of the projected parameter" in {
    val program = "<{test=(λx : Bool.x false),another=if if true then false else true then false else true}.test>"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult.get.-->*() shouldEqual BooleanConstant("false")
  }

  it should "typecheck" in {
    val program = "<{test=(λx : Bool.x false),another=if if true then false else true then false else true}.test>"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a[Success[_]]

    parserResult.get.typecheck() shouldEqual Success(TypeInformations.Bool)
  }

  "An application of a function with a record type" should "parse fine" in {
    val program = "(λx : {test=Bool}.<x.test> {test=false})"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult shouldEqual (Success(
      LambdaApplication(
        LambdaAbstraction(
          TypedLambdaVariable("x", Some(RecordType(Map("test"-> TypeInformations.Bool)))), RecordProjection(TypedLambdaVariable("x"), "test")
        ), Record(Map("test" -> BooleanConstant("false"))))))
  }

  it should "evaluate to false" in {
    val program = "(λx : {test=Bool}.<x.test> {test=false})"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get.-->*() shouldEqual BooleanConstant("false")
  }

  it should "typecheck" in {
    val program = "(λx : {test=Bool}.<x.test> {test=false})"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get.typecheck() shouldEqual Success(TypeInformations.Bool)
  }

  "An if expression with two records" should "parse fine" in {
    val program = "if true then {test=(λx : Bool.x true),another=false} else {test=false,another=true}"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult shouldEqual Success(
      IfExpr(
        BooleanConstant("true"),
        Record(Map(
          "test" ->
            LambdaApplication(
              LambdaAbstraction(TypedLambdaVariable("x", Some(TypeInformations.Bool)), TypedLambdaVariable("x")),
              BooleanConstant("true")),
          "another" -> BooleanConstant("false"))),
        Record(Map("test" -> BooleanConstant("false"), "another" -> BooleanConstant("true")))
      )
    )
  }

  it should "evaluate to the first record" in {
    val program = "if true then {test=(λx : Bool.x true),another=false} else {test=false,another=true}"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get.-->*() shouldEqual Record(Map("test" -> BooleanConstant("true"), "another" -> BooleanConstant("false")))
  }

  it should "typecheck to the record type" in {
    val program = "if true then {test=(λx : Bool.x true),another=false} else {test=false,another=true}"
    val parserResult = new TypedLambdaCalculusSyntax(program).Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get.typecheck() shouldEqual Success(RecordType(Map("test"-> TypeInformations.Bool, "another" -> TypeInformations.Bool)))
  }
}
