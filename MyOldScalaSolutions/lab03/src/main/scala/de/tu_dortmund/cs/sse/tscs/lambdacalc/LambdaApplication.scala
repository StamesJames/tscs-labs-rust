package de.tu_dortmund.cs.sse.tscs.lambdacalc

import de.tu_dortmund.cs.sse.tscs.Expression

/**
  * Represents an application in the λ calculus
  */
case class LambdaApplication(function : Expression, argument : Expression) extends LambdaExpression {
  override def toString: String = "(" + function.toString + " " + argument.toString + ")"
}
