package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references

import de.tu_dortmund.cs.sse.tscs.Expression
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaExpression

case class Assignment(lhs : Expression, rhs : Expression) extends TypedLambdaExpression