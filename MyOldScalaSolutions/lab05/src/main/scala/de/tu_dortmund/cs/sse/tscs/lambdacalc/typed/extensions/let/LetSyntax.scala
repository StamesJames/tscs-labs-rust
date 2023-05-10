package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.let

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.{BasicTypeSyntax, TypedLambdaExpression}
import org.parboiled2.Rule1

trait LetSyntax extends BasicTypeSyntax {
  override def Term: Rule1[TypedLambdaExpression] = rule {
    LetTerm | super.Term
  }

  def LetTerm  = rule {
    "let "~ LambdaVariableTerm ~ "=" ~ Term ~ " in " ~ Term ~> Let
  }

}
