package de.tu_dortmund.cs.sse.tscs

import scala.collection.immutable
import scala.util.{Failure, Success, Try}

/**
  * Basic trait for type checking
  */
trait Typecheck extends Expression {

  def typecheck() : Try[TypeInformation] = typecheck(this, immutable.HashMap())

  def typecheck(expr: Expression, gamma: immutable.Map[Expression, TypeInformation]): Try[TypeInformation] = {
    println("Failure bounce for " + expr)
    Failure(new TypingException(expr))
  }
}
