package de.tu_dortmund.cs.sse.tscs.lambdacalc

import de.tu_dortmund.cs.sse.tscs.{Evaluation, Expression}

/**
  * Specifies the operation semantics of the λ calculus
  */
trait LambdaExpression extends Evaluation {
  override def -->(): Expression = -->(this)

  override def -->(expr: Expression): Expression = {
    //println("Evaluating: " + expr.toString)
    expr match {
    /* E-App1   */ case LambdaApplication(t1, t2) if progressPossible(t1) && !isValue(t1) => LambdaApplication(-->(t1), t2)
    /* E-App2   */ case LambdaApplication(v1, t2) if isValue(v1) && progressPossible(t2) => LambdaApplication(v1, -->(t2))
    /* E-AppAbs */ case LambdaApplication(LambdaAbstraction(x,t12), v2) if isValue(v2) => substitute(matcher(x,v2), t12)
    case λ : LambdaAbstraction => λ
    case _ => null
    }
  }

  def substitute(f : PartialFunction[LambdaVariable, Expression], term : Expression) : Expression = {
    term match {
      case v : LambdaVariable if f.isDefinedAt(v) => f(v)
      case v : LambdaVariable => v
      case LambdaApplication(t1, t2) => LambdaApplication(substitute(f, t1), substitute(f, t2))
      case LambdaAbstraction(v, t) => LambdaAbstraction(v, substitute(f, t))
    }
  }

  def matcher(variable : LambdaVariable,  expression : Expression) : PartialFunction[LambdaVariable, Expression] =
    new PartialFunction[LambdaVariable, Expression] {
      def apply(pv: LambdaVariable) = if (pv == variable) expression else null
      override def isDefinedAt(pv: LambdaVariable): Boolean = (pv == variable)
    }


}
