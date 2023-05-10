package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit

import de.tu_dortmund.cs.sse.tscs.{BaseTypeInformation, TypeInformation}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.{BasicTypeSyntax, TypedLambdaExpression}
import org.parboiled2.Rule1

trait UnitSyntax extends BasicTypeSyntax {
  override def Term: Rule1[TypedLambdaExpression] =  rule {
    UnitLiteral | super.Term
  }

  override def TypeInfo : Rule1[TypeInformation] = rule {
    UnitTypeLiteral | super.TypeInfo
  }

  def UnitLiteral : Rule1[TypedLambdaExpression] = rule {
    capture("unit") ~> ((_ : String) => UnitConstant)
  }

  def UnitTypeLiteral : Rule1[BaseTypeInformation] = rule {
    capture("Unit") ~> ((_ : String) => UnitType)
  }
}