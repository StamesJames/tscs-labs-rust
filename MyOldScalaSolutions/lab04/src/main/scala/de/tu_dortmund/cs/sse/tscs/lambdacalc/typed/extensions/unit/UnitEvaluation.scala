package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit

import de.tu_dortmund.cs.sse.tscs.Expression
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaExpression, LambdaVariable}

trait UnitEvaluation extends LambdaExpression {
  override def substitute(f: PartialFunction[LambdaVariable, Expression], term: Expression): Expression = {
    term match {
      case UnitConstant => UnitConstant
      case _ => super.substitute(f, term)
    }
  }
}
