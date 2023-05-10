package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.{Evaluation, Expression, numexpr}

/**
  * Specifies the operation semantics of the N language
  */
trait NumericExpression extends Evaluation {
  override def -->(): Expression = -->(this)

  override def -->(expr: Expression): Expression = {
    expr match {
      case ValueExpr(v) => ValueExpr(v)
      case Zero => Zero
      case SuccNvExpr(nv) => numexpr.SuccNvExpr(nv)
      case SuccExpr(subExpr) if isValue(subExpr) => numexpr.SuccNvExpr(subExpr)
      /* E-IfTrue      */ case IfExpr(ValueExpr("true"), iftrue, iffalse) => iftrue
      /* E-IfFalse     */ case IfExpr(ValueExpr("false"), iftrue, iffalse) => iffalse
      /* E-If          */ case IfExpr(cond, iftrue, iffalse) => IfExpr(-->(cond), iftrue, iffalse)
      /* E-Succ        */ case SuccExpr(subexpr) if !isValue(subexpr) && progressPossible(subexpr) => SuccExpr(-->(subexpr))
      /* E-PredZero    */ case PredExpr(Zero) => Zero
      /* E-PredSucc    */ case PredExpr(SuccNvExpr(nv)) => nv
      /* E-Pred        */ case PredExpr(subexpr) if !isValue(subexpr) && progressPossible(subexpr) => PredExpr(-->(subexpr))
      /* E-IsZeroZero  */ case IsZeroExpr(Zero) => ValueExpr("true")
      /* E-IsZeroSucc  */ case IsZeroExpr(SuccNvExpr(nv)) => ValueExpr("false")
      /* E-IsZero      */ case IsZeroExpr(subexpr) if !isValue(subexpr) && progressPossible(subexpr) => IsZeroExpr(-->(subexpr))
      case _ => null
    }
  }
}
