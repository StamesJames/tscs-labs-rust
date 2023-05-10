package de.tu_dortmund.cs.sse.tscs.lambdacalc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success

/**
  * Checks for Lambda Calculus Expressions
  */
class LambdaExpressionCheck extends AnyFlatSpec with Matchers {
  "λx.x" should "be valid" in {
    var parserResult = new LambdaCalculusSyntax("λx.x").Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get shouldBe new LambdaAbstraction(new UntypedLambdaVariable("x"), new UntypedLambdaVariable("x"))
  }

  "λx.λy.((x y) x)" should "be valid" in {
    var parserResult = new LambdaCalculusSyntax("λx.λy.((x y) x)").Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get shouldBe new LambdaAbstraction(UntypedLambdaVariable("x"),
                                  new LambdaAbstraction(UntypedLambdaVariable("y"),
                                    LambdaApplication(
                                        LambdaApplication(UntypedLambdaVariable("x"),UntypedLambdaVariable("y"))
                                              ,UntypedLambdaVariable("x"))))
  }

  "λz.λx.λy.(x (y z))" should "be valid" in {
    var parserResult = new LambdaCalculusSyntax("λz.λx.λy.(x (y z))").Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get shouldBe
      new LambdaAbstraction(UntypedLambdaVariable("z"),
        LambdaAbstraction(UntypedLambdaVariable("x"),
        LambdaAbstraction(UntypedLambdaVariable("y"),
          LambdaApplication(
            UntypedLambdaVariable("x"),
            LambdaApplication(UntypedLambdaVariable("y"),UntypedLambdaVariable("z"))))))
  }

  "(λx.x x)" should "be valid" in {
    var parserResult = new LambdaCalculusSyntax("(λx.x x)").Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get shouldBe new LambdaApplication(
          LambdaAbstraction(UntypedLambdaVariable("x"), UntypedLambdaVariable("x")),
          UntypedLambdaVariable("x"))
  }

  "(λx.λy.((x y) x) c)" should "be valid" in {
    var parserResult = new LambdaCalculusSyntax("(λx.λy.((x y) x) c)").Term.run()
    parserResult shouldBe a [Success[_]]
    parserResult.get shouldBe new LambdaApplication(
      LambdaAbstraction(UntypedLambdaVariable("x"),
        LambdaAbstraction(UntypedLambdaVariable("y"),
          LambdaApplication(
            LambdaApplication(
              UntypedLambdaVariable("x"),
              UntypedLambdaVariable("y")),
            UntypedLambdaVariable("x")))),
      UntypedLambdaVariable("c"))
  }

  it should "evaluate to a substituted form" in {
    var parserResult = new LambdaCalculusSyntax("(λx.λy.((x y) x) c)").Term.run()

    parserResult.get.-->*() shouldBe new LambdaAbstraction(UntypedLambdaVariable("y"),
          LambdaApplication(
            LambdaApplication(
              UntypedLambdaVariable("c"),
              UntypedLambdaVariable("y")),
            UntypedLambdaVariable("c")))
  }

}
