package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.Expression

/**
  * Represents if-Expressions in the N language
  */
case class IfExpr(condition: Expression, IfTrue: Expression, IfFalse: Expression) extends NumericExpression
