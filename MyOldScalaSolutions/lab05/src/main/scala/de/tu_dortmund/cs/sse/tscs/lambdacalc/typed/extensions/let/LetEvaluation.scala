package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.let

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.let
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references.StatefulEvaluation
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaExpression, LambdaVariable}
import de.tu_dortmund.cs.sse.tscs.{Expression, Value}

trait LetEvaluation extends LambdaExpression with StatefulEvaluation {
  override def runStep(input: (Expression, List[Value])): (Expression, List[Value]) = {
    input match {
      /* E-LetV */
      case (Let(variable: LambdaVariable, substitutionTerm, innerTerm), μ) if isValue(substitutionTerm) => (substitute(matcher(variable, substitutionTerm), innerTerm), μ)

    /* E-Let */
      case (Let(variable, substitutionTerm, innerTerm), μ) if progressPossible((substitutionTerm, μ)) => {
        val substitutionStep = runStep((substitutionTerm, μ))
        (let.Let(variable, substitutionStep._1, innerTerm), substitutionStep._2)
      }

    case _ => super.runStep(input)
    }
  }

  override def -->(expr: Expression): Expression = {
    expr match {
      /* E-LetV */ case Let(variable: LambdaVariable, substitutionTerm, innerTerm) if isValue(substitutionTerm) => substitute(matcher(variable, substitutionTerm), innerTerm)
      /* E-Let */ case Let(variable, substitutionTerm, innerTerm) if progressPossible(substitutionTerm) => Let(variable, -->(substitutionTerm), innerTerm)
      case _ => super.-->(expr)
    }
  }

  override def substitute(f: PartialFunction[LambdaVariable, Expression], term: Expression): Expression = {
    term match {
      case Let(variable, substitutionTerm, innerTerm) => Let(variable, substitute(f, substitutionTerm), substitute(f, innerTerm))
      case _ => super.substitute(f, term)
    }
  }
}
