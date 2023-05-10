package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, Typecheck}

import scala.collection.immutable
import scala.util.Try

trait StatefulTypecheck extends Typecheck {
  override def typecheck(): Try[TypeInformation] = typecheck(this, immutable.HashMap(), immutable.HashMap())

  override def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation]): Try[TypeInformation] =
    typecheck(expr, gamma, immutable.HashMap())

  def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation], sigma: Map[Location, TypeInformation]): Try[TypeInformation] =
    super.typecheck(expr, gamma)
}
