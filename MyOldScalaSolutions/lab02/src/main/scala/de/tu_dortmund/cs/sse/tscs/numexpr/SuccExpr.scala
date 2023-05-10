package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.Expression

/**
  * Represents a succ operation in the N language
  */
case class SuccExpr (subterm : Expression) extends TypedNumericExpression
