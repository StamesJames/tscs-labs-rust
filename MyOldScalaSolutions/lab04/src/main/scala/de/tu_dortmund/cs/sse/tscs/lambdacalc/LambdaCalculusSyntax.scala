package de.tu_dortmund.cs.sse.tscs.lambdacalc

import org.parboiled2.{CharPredicate, Parser, ParserInput, Rule1}

/**
  * A syntax definition for the λ calculus
  */
class LambdaCalculusSyntax (val input : ParserInput) extends Parser {
  def InputLine = rule {
    Term | EOI
  }

  def Term: Rule1[LambdaExpression] = rule {
    LambdaVariableTerm | LambdaAbstractionTerm | LambdaApplicationTerm
  }

  def LambdaVariableTerm : Rule1[LambdaVariable] = rule {
    capture(oneOrMore(CharPredicate.Alpha)) ~> UntypedLambdaVariable
  }

  def LambdaAbstractionTerm : Rule1[LambdaAbstraction] = rule {
    'λ' ~ LambdaVariableTerm ~ "." ~ Term ~> LambdaAbstraction
  }

  def LambdaApplicationTerm : Rule1[LambdaApplication] = rule {
    "(" ~ Term ~ " " ~ Term ~ ")" ~> LambdaApplication
  }
}
