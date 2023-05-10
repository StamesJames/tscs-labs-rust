package de.tu_dortmund.cs.sse.tscs.lambdacalc

import de.tu_dortmund.cs.sse.tscs.Value

/**
  * Represents a variable in the λ calculus
  */
class LambdaVariable(variable : String) extends Value(variable) with LambdaExpression {
  override def toString: String = variable
}

case class UntypedLambdaVariable(variable : String) extends LambdaVariable(variable)

