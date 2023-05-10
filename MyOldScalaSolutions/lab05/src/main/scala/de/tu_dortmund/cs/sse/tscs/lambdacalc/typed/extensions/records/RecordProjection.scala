package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records

import de.tu_dortmund.cs.sse.tscs.Expression
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaExpression

case class RecordProjection(record : Expression, projection : String) extends TypedLambdaExpression
