package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional

import de.tu_dortmund.cs.sse.tscs.Expression
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaExpression, LambdaVariable}

trait ConditionalEvaluation extends LambdaExpression {
  override def -->(expr: Expression): Expression = {
    expr match {
      case x : BooleanConstant => x
      /* E-IfTrue  */ case IfExpr(BooleanConstant("true", _), iftrue, iffalse) => iftrue
      /* E-IfFalse */ case IfExpr(BooleanConstant("false", _), iftrue, iffalse) => iffalse
      /* E-If      */ case IfExpr(cond, iftrue, iffalse) if progressPossible(cond) => conditional.IfExpr(-->(cond), iftrue, iffalse)
      case _ => super.-->(expr)
    }
  }

  override def substitute(f: PartialFunction[LambdaVariable, Expression], term: Expression): Expression = {
    term match {
      case IfExpr(cond, iftrue, iffalse) => conditional.IfExpr(substitute(f, cond), substitute(f, iftrue), substitute(f, iffalse))
      case _ => super.substitute(f, term)
    }
  }


}
