package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.Value

/**
  * Represents values in the N language
  */
case class ValueExpr(v : String) extends Value(v) with TypedNumericExpression
