package de.tu_dortmund.cs.sse.tscs.binexpr

import de.tu_dortmund.cs.sse.tscs.Expression

/**
  * Represents if-Expressions in the B language
  */
case class IfExpr(condition: Expression, IfTrue: Expression, IfFalse: Expression) extends BinaryExpression
