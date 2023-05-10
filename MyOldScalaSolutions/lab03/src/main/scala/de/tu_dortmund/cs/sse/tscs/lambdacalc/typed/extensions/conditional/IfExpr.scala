package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional

import de.tu_dortmund.cs.sse.tscs.Expression
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaExpression

case class IfExpr(condition: Expression, IfTrue: Expression, IfFalse: Expression) extends TypedLambdaExpression with ConditionalEvaluation
