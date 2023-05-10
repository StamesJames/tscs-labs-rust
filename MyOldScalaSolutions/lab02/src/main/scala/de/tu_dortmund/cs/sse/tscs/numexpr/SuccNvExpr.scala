package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.Expression

/**
  * Represents a term in the form of succ nv
  */
case class SuccNvExpr (nv : Expression) extends NumericValue("succ " + nv.toString) with TypedNumericExpression
