package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.{BasicTypeSyntax, TypedLambdaExpression}
import org.parboiled2.Rule1

trait ConditionalSyntax extends BasicTypeSyntax {

  override def Term: Rule1[TypedLambdaExpression] =  rule {
    TrueTerm| FalseTerm | IfTerm | super.Term
  }

  def TrueTerm = rule {
    capture("true") ~> (c => BooleanConstant(c, None))
  }

  def FalseTerm = rule {
    capture("false") ~> (c => BooleanConstant(c, None))
  }

  def IfTerm = rule {
    "if " ~ Term ~ " then " ~ Term ~ " else " ~ Term ~> IfExpr
  }
}
