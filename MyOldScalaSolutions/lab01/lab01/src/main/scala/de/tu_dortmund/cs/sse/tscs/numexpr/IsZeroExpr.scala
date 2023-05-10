package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.Expression

/**
  * Represents a iszero operation in the N language
  */
case class IsZeroExpr(subterm : Expression) extends NumericExpression
