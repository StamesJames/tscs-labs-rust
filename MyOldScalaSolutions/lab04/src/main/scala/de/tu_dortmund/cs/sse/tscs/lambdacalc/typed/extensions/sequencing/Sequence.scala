package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing

import de.tu_dortmund.cs.sse.tscs.Expression
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaExpression

case class Sequence(firstTerm : Expression, secondTerm : Expression) extends TypedLambdaExpression