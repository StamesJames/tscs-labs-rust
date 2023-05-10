package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, Typecheck}

import scala.util.{Success, Try}

trait UnitTypecheck extends Typecheck {
  override def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation]): Try[TypeInformation] = {
    expr match {
      case UnitConstant => Success(UnitType)
      case _ => super.typecheck(expr, gamma)
    }
  }

}
