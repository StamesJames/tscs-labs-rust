package de.tu_dortmund.cs.sse.tscs.binexpr

import de.tu_dortmund.cs.sse.tscs.Value

/**
  * Represents values in the B language
  */
case class ValueExpr(v : String) extends Value(v) with BinaryExpression
