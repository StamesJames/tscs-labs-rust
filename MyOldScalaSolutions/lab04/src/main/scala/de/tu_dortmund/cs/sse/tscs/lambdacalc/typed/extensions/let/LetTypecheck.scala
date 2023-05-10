package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.let

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, Typecheck}

import scala.util.Try

trait LetTypecheck extends Typecheck {
  override def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation]): Try[TypeInformation] = {
    expr match {
        // TODO: Add typing rules for let-bindings
        case Let(variable, substitutionTerm, innerTerm) 
            if typecheck(substitutionTerm, gamma).isSuccess
            && typecheck(innerTerm, gamma + (variable -> typecheck(substitutionTerm, gamma).get)).isSuccess => 
                typecheck(innerTerm, gamma + (variable -> typecheck(substitutionTerm, gamma).get))
       case _ => super.typecheck(expr, gamma)
    }
  }
}
