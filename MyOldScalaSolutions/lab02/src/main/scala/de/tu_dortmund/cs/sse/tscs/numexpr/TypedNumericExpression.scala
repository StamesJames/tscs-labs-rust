package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, Typecheck, TypingException, TypeInformations}

import scala.util.{Failure, Success, Try}

/**
  * Reuses the operation semantics of the N language and extends it with type checking
  */
trait TypedNumericExpression extends NumericExpression with Typecheck {

  override def typecheck(): Try[TypeInformation] = typecheck(this)


  override def typecheck(expr: Expression): Try[TypeInformation] = {
    val sBool = Success(TypeInformations.Bool)
    val sNat  = Success(TypeInformations.Nat)
    expr match {
      // Axioms B-Language
      case ValueExpr("true")  => sBool
      case ValueExpr("false") => sBool
      // Axioms N-Extension
      case ValueExpr("0")     => sNat
      case Zero               => sNat
      // Rules B-Language
      case IfExpr(cond, iftrue, iffalse) if (typecheck(cond) == sBool && typecheck(iftrue) == typecheck(iffalse)) => typecheck(iftrue)
      // Rules N-Extension
      case SuccNvExpr(nv)   if (typecheck(nv) == sNat) => sNat
      case SuccExpr(nv)     if (typecheck(nv) == sNat) => sNat
      case PredExpr(nv)     if (typecheck(nv) == sNat) => sNat
      case IsZeroExpr(nv)   if (typecheck(nv) == sNat) => sBool
      case _ => new Failure[TypeInformation](null)
    }
  }

}
