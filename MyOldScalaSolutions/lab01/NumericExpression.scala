package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.{Evaluation, Expression}

/**
  * Specifies the operation semantics of the N language
  */
trait NumericExpression extends Evaluation {
  override def -->(): Expression = -->(this)

  override def -->(expr: Expression): Expression = {
    expr match {
      // Values
      case SuccNvExpr(nv) => SuccNvExpr(nv)
      case SuccExpr(ValueExpr("0")) => SuccNvExpr(ValueExpr("0"))
      case SuccExpr(SuccNvExpr(nv)) => SuccNvExpr(SuccNvExpr(nv))
      case ValueExpr("true") => ValueExpr("true")
      case ValueExpr("false") => ValueExpr("false")
      case ValueExpr("0") => ValueExpr("0")
      // Axioms of B-Language
      case IfExpr(ValueExpr("true"), iftrue, iffalse) => iftrue
      case IfExpr(ValueExpr("false"), iftrue, iffalse) => iffalse
      // Axioms of N-Language-Extension
      case PredExpr(ValueExpr("0")) => ValueExpr("0")
      case PredExpr(SuccExpr(nv)) => nv
      case PredExpr(SuccNvExpr(nv)) => nv
      case IsZeroExpr(ValueExpr("0")) => ValueExpr("true")
      case IsZeroExpr(SuccNvExpr(nv)) => ValueExpr("false")
      // Rules of B-Language
      case IfExpr(nestif, iftrue, iffalse) if (progressPossible(nestif) && !isFinalResult(nestif)) => IfExpr(-->(nestif), iftrue, iffalse)
      // Rules of N-Language-Extension
      case SuccExpr(t)    if (progressPossible(t) && !isFinalResult(t)) => SuccExpr(-->(t))
      case PredExpr(t)    if (progressPossible(t) && !isFinalResult(t)) => PredExpr(-->(t))
      case IsZeroExpr(t)  if (progressPossible(t) && !isFinalResult(t)) => IsZeroExpr(-->(t))
      // Default case
      case _ => null
    }
  }
}
