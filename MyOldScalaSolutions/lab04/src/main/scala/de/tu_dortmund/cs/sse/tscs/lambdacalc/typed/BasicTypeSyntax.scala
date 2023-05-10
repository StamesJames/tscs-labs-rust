package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed

import de.tu_dortmund.cs.sse.tscs.{BaseTypeInformation, FunctionType, TypeInformation}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaAbstraction, LambdaApplication, LambdaCalculusSyntax, LambdaExpression, UntypedLambdaVariable}

import org.parboiled2.{CharPredicate, Rule1}

/**
  * Syntax definition for basic types.
  */
trait BasicTypeSyntax extends LambdaCalculusSyntax{

  override def Term: Rule1[TypedLambdaExpression] = rule {
    LambdaAbstractionTerm ~> (widen(_ : LambdaAbstraction)) |
      LambdaApplicationTerm ~> (widen(_ : LambdaApplication)) |
      LambdaVariableTerm
  }

  override def LambdaVariableTerm : Rule1[TypedLambdaVariable] = rule {
    capture(oneOrMore(CharPredicate.Alpha)) ~ optional (" : " ~ TypeInfo) ~> ((v : String, t : Option[TypeInformation]) => TypedLambdaVariable(v,t))
  }

  def TypeInfo = rule {
    BaseTypeInfo | BoolTypeInfo | FunctionTypeInfo
  }

  def BaseTypeInfo : Rule1[TypeInformation] = rule {
    capture("A") ~> BaseTypeInformation
  }

  def BoolTypeInfo : Rule1[TypeInformation] = rule {
    capture("Bool") ~> BaseTypeInformation
  }

  def FunctionTypeInfo : Rule1[TypeInformation] = rule {
    "[" ~ TypeInfo ~ "->" ~ TypeInfo ~ "]" ~> FunctionType
  }


  def widen(expression: LambdaExpression) : TypedLambdaExpression = {
    expression match {
      case UntypedLambdaVariable(v) => new TypedLambdaVariable(v, Option.empty)
      case LambdaApplication(f,a) => new LambdaApplication(f,a) with TypedLambdaExpression
      case LambdaAbstraction(v,t) => new LambdaAbstraction(v,t) with TypedLambdaExpression
    }
  }
}
