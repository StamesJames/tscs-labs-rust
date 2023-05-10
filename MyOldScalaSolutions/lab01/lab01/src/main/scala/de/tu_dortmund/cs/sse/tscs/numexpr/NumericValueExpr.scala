package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.Value

/**
  * Represents numeric values in the N-language
  */
case class NumericValueExpr(v : String) extends Value(v) with NumericExpression
