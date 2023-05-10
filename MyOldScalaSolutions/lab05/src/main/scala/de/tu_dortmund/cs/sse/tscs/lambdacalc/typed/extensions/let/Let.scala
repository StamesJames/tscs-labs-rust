package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.let

import de.tu_dortmund.cs.sse.tscs.Expression
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaExpression

case class Let(variable : Expression, substitutionTerm : Expression, innerTerm : Expression) extends TypedLambdaExpression
