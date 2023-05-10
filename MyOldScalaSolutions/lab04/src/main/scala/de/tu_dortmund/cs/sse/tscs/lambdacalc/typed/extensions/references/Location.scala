package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references

import de.tu_dortmund.cs.sse.tscs.Value
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaExpression

case class Location (position: Integer) extends Value(s"l$position") with TypedLambdaExpression
