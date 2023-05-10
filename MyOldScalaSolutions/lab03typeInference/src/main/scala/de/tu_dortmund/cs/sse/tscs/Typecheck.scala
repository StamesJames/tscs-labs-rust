package de.tu_dortmund.cs.sse.tscs

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
  * Basic trait for type checking
  */
trait Typecheck extends Expression {

  def storeAndWrap(e: Expression, t: TypeInformation, gamma: mutable.HashMap[Expression, TypeInformation]): Try[TypeInformation] = {
    gamma.put(e, t);
    Success(t)
  }

  def typecheck() : Try[TypeInformation] = typecheck(this, mutable.HashMap())

  def typecheck(expr : Expression, gamma: mutable.HashMap[Expression, TypeInformation]) : Try[TypeInformation] = {
    Failure(new TypingException(expr))
  }
}
