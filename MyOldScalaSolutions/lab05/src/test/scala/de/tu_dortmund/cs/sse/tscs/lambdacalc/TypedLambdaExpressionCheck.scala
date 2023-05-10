package de.tu_dortmund.cs.sse.tscs.lambdacalc

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaCalculusSyntax
import de.tu_dortmund.cs.sse.tscs.{FunctionType, TypeInformations}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

/**
  * Checks for the type checker
  */
class TypedLambdaExpressionCheck extends AnyFlatSpec with Matchers {
  "λx.x : A" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("λx.x : A").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldBe a[Success[_]]
  }

  "λx : [A->A].λy : A.((x y) x)" should "successfully parse" in {
    var parserResult = new TypedLambdaCalculusSyntax("λx : [A->A].λy : A.((x y) x)").Term.run()
    parserResult shouldBe a[Success[_]]
  }

  "λx : [A->A].λy : A.((x y) x)" should "not successfully typecheck" in {
    new TypedLambdaCalculusSyntax("x : A").Term.run().get.typecheck().get shouldEqual TypeInformations.AbstractBaseType

    var parserResult = new TypedLambdaCalculusSyntax("λx : [A->A].λy : A.((x y) x)").Term.run()
    parserResult.get.typecheck() shouldBe a[Failure[_]]
  }

  "λz : A.λx : [A->A].λy : [A->A].(x (y z))" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("λz : A.λx : [A->A].λy : [A->A].(x (y z))").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldBe a[Success[_]]
  }

  "(λx.x x)" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("(λx.x x)").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldBe a[Success[_]]
  }

  "λx : [[A->A]->[A->A]].λy : [A->A].((x y) c)" should "successfully typecheck" in {
    var parserResult = new TypedLambdaCalculusSyntax("λx : [[A->A]->[A->A]].λy : [A->A].((x y) c)").Term.run()
    parserResult shouldBe a[Success[_]]
    parserResult.get.typecheck() shouldBe a[Success[_]]
  }

  "λx : [[A->A]->[A->A]].λy : [A->A].((x y) c)" should "evaluate correctly" in {
    val environment = new TypedLambdaCalculusSyntax("λx : [[A->A]->[A->A]].λy : [A->A].((x y) c)").Term.run()
    environment shouldBe a[Success[_]]
    val expression = environment.get.-->*()
    expression shouldNot be(null)
  }

  "λz : A.λx : [A->A].λy : [A->A].(x (y z))" should "typecheck as [A->[[A->A]->[[A->A]->A]]]" in {
    val env = new TypedLambdaCalculusSyntax("λz : A.λx : [A->A].λy : [A->A].(x (y z))").Term.run()
    env shouldBe a[Success[_]]
    env.get.typecheck() shouldEqual
      Success(
        FunctionType(
          TypeInformations.AbstractBaseType,
          FunctionType(
            FunctionType(TypeInformations.AbstractBaseType, TypeInformations.AbstractBaseType),
            FunctionType(
              FunctionType(TypeInformations.AbstractBaseType, TypeInformations.AbstractBaseType),
              TypeInformations.AbstractBaseType))))
  }

  "λx : [[A->A]->[A->A]].λy : [A->A].((x y) c)" should "typecheck as [[[A->A]->[A->A]]->[[A->A]->A]]" in {
     val env = new TypedLambdaCalculusSyntax("λx : [[A->A]->[A->A]].λy : [A->A].((x y) c)").Term.run()
    env shouldBe a[Success[_]]
    env.get.typecheck() shouldEqual
      Success(
        FunctionType(
          FunctionType(
            FunctionType(TypeInformations.AbstractBaseType, TypeInformations.AbstractBaseType),
            FunctionType(TypeInformations.AbstractBaseType, TypeInformations.AbstractBaseType)),
          FunctionType(
            FunctionType(TypeInformations.AbstractBaseType, TypeInformations.AbstractBaseType),
            TypeInformations.AbstractBaseType)))
  }
}
