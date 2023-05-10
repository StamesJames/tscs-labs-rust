package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, Typecheck}

import scala.util.Try

trait SequenceTypecheck extends Typecheck {
  override def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation]): Try[TypeInformation] = {
    expr match {
      /* T-Seq */case Sequence(first, second)
        if typecheck(first, gamma).isSuccess &&
          //typecheck(first, gamma).get.equals(UnitType) &&
          typecheck(second, gamma).isSuccess
      => typecheck(second, gamma)
      case _ => super.typecheck(expr, gamma)
    }
  }
}
