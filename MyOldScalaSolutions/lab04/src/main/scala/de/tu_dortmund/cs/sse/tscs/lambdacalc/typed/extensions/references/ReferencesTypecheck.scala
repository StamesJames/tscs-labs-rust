package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit.UnitType

import scala.util.{Success, Try}

trait ReferencesTypecheck extends StatefulTypecheck {
  override def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation], sigma: Map[Location, TypeInformation]): Try[TypeInformation] = {
    expr match {
      // TODO: Add typing rules for references
      case Allocation(term) if typecheck(term, gamma, sigma).isSuccess => Success( ReferenceType(typecheck(term, gamma).get) )

      case Assignment(lhs, rhs) 
        if (typecheck(lhs, gamma, sigma) match {
          case Success(ReferenceType(t)) if (typecheck(rhs, gamma, sigma) match {
            case Success(`t`) => true
            case _ => false 
          }) => true
          case _ => false
        }) => Success(UnitType)

      case Dereferencing(term) 
        if (typecheck(term, gamma, sigma) match {
          case Success(ReferenceType(x)) => true
          case _ => false 
        }) => Success( (typecheck(term, gamma, sigma) match { case Success(ReferenceType(x)) => x }) )

      case _ => super.typecheck(expr, gamma, sigma)
    }
  }
}
