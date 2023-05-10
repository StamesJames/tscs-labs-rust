package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional

import de.tu_dortmund.cs.sse.tscs.TypeInformation
import de.tu_dortmund.cs.sse.tscs.lambdacalc.LambdaVariable
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaExpression

case class BooleanConstant(constant : String, typeAnnotation: Option[TypeInformation]) extends LambdaVariable(constant) with TypedLambdaExpression {

}
