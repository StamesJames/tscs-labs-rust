package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.{Expression, Value}

/**
  * Represents a term in the form of succ nv
  */
case class SuccNvExpr (nv : Expression) extends Value("succ " + nv.toString) with NumericExpression
