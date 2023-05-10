package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed

import de.tu_dortmund.cs.sse.tscs.TypeInformation
import de.tu_dortmund.cs.sse.tscs.lambdacalc.LambdaVariable

/**
  * Represents a variable in the λ calculus
  */
case class TypedLambdaVariable(val variable : String, typeAnnotation: Option[TypeInformation]) extends LambdaVariable(variable) with TypedLambdaExpression {
  override def toString: String = { variable + {
    typeAnnotation match {
      case o if !o.isEmpty => " : " + o.get.toString
      case _ => ""
    }}
  }


  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case that : TypedLambdaVariable =>  that.variable.equals(this.variable)
      case _ => false
    }
  }

  override def hashCode(): Int = {
    val prime = 179
    var result = 1
    result = prime * result + variable.hashCode
    return result
  }
}

