package de.tu_dortmund.cs.sse.tscs.lambdacalc

import de.tu_dortmund.cs.sse.tscs.{Expression, Value}

/**
  * Represents an abstraction in the λ calculus
  */
case class LambdaAbstraction(variable : LambdaVariable, term : Expression) extends Value("λ") with LambdaExpression {
  override def toString: String = "λ" + variable + "." + term.toString
}