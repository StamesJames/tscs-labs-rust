package de.tu_dortmund.cs.sse.tscs.binexpr

import de.tu_dortmund.cs.sse.tscs.{Evaluation, Expression}

/**
  * Specifies the operational semantics of the B language
  */
trait BinaryExpression extends Evaluation {
  override def -->(): Expression = -->(this)

  override def -->(expr: Expression): Expression =
    expr match {
      // Axiomes
      case IfExpr(ValueExpr("true"), iftrue, iffalse) => iftrue
      case IfExpr(ValueExpr("false"), iftrue, iffalse) => iffalse
      // Rules
      case IfExpr(nestif, iftrue, iffalse) if (progressPossible(nestif) && !isFinalResult(nestif)) => IfExpr(-->(nestif), iftrue, iffalse)
      // Values
      case ValueExpr("true") => ValueExpr("true")
      case ValueExpr("false") => ValueExpr("false")
      // Default case
      case _ => null
    }

}
