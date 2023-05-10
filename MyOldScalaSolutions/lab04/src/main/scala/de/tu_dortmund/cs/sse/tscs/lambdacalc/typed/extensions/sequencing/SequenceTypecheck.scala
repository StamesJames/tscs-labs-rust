package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, Typecheck}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit.UnitType

import scala.util.{Try, Success}

trait SequenceTypecheck extends Typecheck {
  override def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation]): Try[TypeInformation] = {
    expr match {
        // TODO: Add typing rules for sequences
      case Sequence(first_term, second_term) if typecheck(first_term, gamma).isSuccess => typecheck(second_term, gamma)
      case _ => super.typecheck(expr, gamma)
    }
  }
}
