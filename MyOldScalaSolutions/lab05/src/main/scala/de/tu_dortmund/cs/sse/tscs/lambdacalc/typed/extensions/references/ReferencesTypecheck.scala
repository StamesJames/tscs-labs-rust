package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit.UnitType
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaAbstraction, LambdaApplication}
import de.tu_dortmund.cs.sse.tscs.{Expression, FunctionType, TypeInformation}

import scala.util.{Success, Try}

trait ReferencesTypecheck extends StatefulTypecheck {
  override def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation], sigma: Map[Location, TypeInformation]): Try[TypeInformation] = {
    expr match {
      /* T-Abs */
      case abs: LambdaAbstraction
        if typecheck(abs.variable, gamma, sigma).isSuccess && typecheck(abs.term,  gamma + (abs.variable -> typecheck(abs.variable, gamma).get), sigma).isSuccess
      => Success(FunctionType(typecheck(abs.variable, gamma, sigma).get, typecheck(abs.term, gamma + (abs.variable -> typecheck(abs.variable, gamma).get), sigma).get))

      /* T-App */
      case app: LambdaApplication if {
        (typecheck(app.function, gamma, sigma), typecheck(app.argument, gamma, sigma)) match {
          case (Success(ft: FunctionType), Success(at)) => at == ft.sourceType
          case e => false
        }
      }
      => Success(typecheck(app.function, gamma, sigma).get.asInstanceOf[FunctionType].targetType)

        /* T-Loc */
      case l : Location if sigma.contains(l)
        => Success(ReferenceType(sigma(l)))

        /* T-Ref */
      case a : Allocation if typecheck(a.term, gamma, sigma).isSuccess
        => Success(ReferenceType(typecheck(a.term, gamma, sigma).get))

        /* T-Deref */
      case d : Dereferencing if {
        val termType = typecheck(d.term, gamma, sigma)
        termType.isSuccess && termType.get.isInstanceOf[ReferenceType]
      }
        => Success(typecheck(d.term, gamma, sigma).get.asInstanceOf[ReferenceType].baseType)
        /* T-Assign */
      case a : Assignment if {
        val lht = typecheck(a.lhs, gamma, sigma)
        val rht = typecheck(a.rhs, gamma, sigma)
        lht.isSuccess && rht.isSuccess
      }
        => Success(UnitType)

      case _ => super.typecheck(expr, gamma, sigma)
    }
  }
}
