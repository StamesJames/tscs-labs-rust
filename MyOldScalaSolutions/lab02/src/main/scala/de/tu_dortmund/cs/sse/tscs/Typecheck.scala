package de.tu_dortmund.cs.sse.tscs

import scala.util.{Failure, Try}

/**
  * Basic trait for type checking
  */
trait Typecheck extends Expression {

  def typecheck() : Try[TypeInformation] = typecheck(this)

  def typecheck(expr : Expression) : Try[TypeInformation] = {
    Failure(new TypingException(expr))
  }
}
