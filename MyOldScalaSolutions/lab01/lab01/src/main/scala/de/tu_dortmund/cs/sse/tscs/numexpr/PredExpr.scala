package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.Expression

/**
  * Represents a pred operation in the N language
  */
case class PredExpr(subterm : Expression) extends NumericExpression
