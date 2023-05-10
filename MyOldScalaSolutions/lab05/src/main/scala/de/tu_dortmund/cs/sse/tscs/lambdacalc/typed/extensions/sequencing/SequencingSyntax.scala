package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.{BasicTypeSyntax, TypedLambdaExpression}
import org.parboiled2.Rule1

trait SequencingSyntax extends BasicTypeSyntax {

  override def Term: Rule1[TypedLambdaExpression] =  rule {
    SequencedTerm | super.Term
  }

  def SequencedTerm = rule {
    "{" ~ Term ~ ";" ~ Term ~ "}" ~> Sequence
  }
}
