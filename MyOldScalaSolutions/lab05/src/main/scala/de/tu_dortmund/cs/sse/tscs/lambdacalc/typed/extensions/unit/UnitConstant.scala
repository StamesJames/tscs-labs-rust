package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit

import de.tu_dortmund.cs.sse.tscs.lambdacalc.LambdaVariable
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaExpression

case object UnitConstant extends LambdaVariable("unit") with TypedLambdaExpression
