package de.tu_dortmund.cs.sse.tscs.binexpr

import org.parboiled2._

/**
  * Defines the syntax for the binary expression language B.
  * @param input
  */
class BinaryExpressionSyntax(val input: ParserInput) extends Parser {
  def InputLine = rule {
    Term | EOI
  }

  def Term: Rule1[BinaryExpression] = rule {
    ValueTerm | IfTerm
  }

  def ValueTerm = rule {
    TrueTerm | FalseTerm
  }

  def TrueTerm = rule {
    capture("true") ~> ValueExpr
  }

  def FalseTerm = rule {
    capture("false") ~> ValueExpr
  }

  def IfTerm = rule {
    "if " ~ Term ~ " then " ~ Term ~ " else " ~ Term ~> IfExpr
  }
}

