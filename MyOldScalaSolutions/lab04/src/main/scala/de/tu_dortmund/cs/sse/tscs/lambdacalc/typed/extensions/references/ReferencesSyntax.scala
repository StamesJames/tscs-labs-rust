package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references

import de.tu_dortmund.cs.sse.tscs.TypeInformation
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.{BasicTypeSyntax, TypedLambdaExpression}
import org.parboiled2.Rule1

trait ReferencesSyntax extends BasicTypeSyntax {

  override def Term: Rule1[TypedLambdaExpression] = rule {
    AllocationTerm | DerefencingTerm | AssignmentTerm | super.Term
  }

  def AllocationTerm = rule {
    "<" ~ "ref " ~ Term ~ ">" ~> Allocation
  }

  def DerefencingTerm = rule {
    "<" ~ "!" ~ Term ~ ">" ~> Dereferencing
  }

  def AssignmentTerm = rule {
    "<" ~ Term ~ ":=" ~ Term ~ ">" ~> Assignment
  }

  override def TypeInfo : Rule1[TypeInformation] = rule {
    ReferenceTypeLiteral | super.TypeInfo
  }

  def ReferenceTypeLiteral = rule {
    "Ref " ~ TypeInfo ~> ReferenceType
  }

}
