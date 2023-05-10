package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, TypeInformations, Typecheck}

import scala.util.{Success, Try}

trait ConditionalTypeCheck extends Typecheck {

  def T_If_Premise(cond: IfExpr, gamma: Map[Expression, TypeInformation]) : Boolean = {
    val conditionType = typecheck(cond.condition, gamma)
    val truePartType = typecheck(cond.IfTrue, gamma)
    val falsePartType = typecheck(cond.IfFalse, gamma)

    if (!(conditionType.isSuccess && truePartType.isSuccess && falsePartType.isSuccess)) return false
    if (!conditionType.get.equals(TypeInformations.Bool)) return false
    if (!truePartType.get.equals(falsePartType.get)) return false

    true
  }

  override def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation]): Try[TypeInformation] = {
    var result = expr match {

      // T-True
      case v: BooleanConstant if T_True_Premise(v)
      => Success(TypeInformations.Bool)

      // T-False
      case v: BooleanConstant if T_False_Premise(v)
      => Success(TypeInformations.Bool)

      // T-If
      case cond : IfExpr if T_If_Premise(cond, gamma) => typecheck(cond.IfTrue, gamma)

      case _ => super.typecheck(expr, gamma)
    }

    result
  }

  private def T_False_Premise(v: BooleanConstant) =
    v.constant == "false" &&
      (v.typeAnnotation match {
        case None => true
        case Some(t) => t == TypeInformations.Bool
        case _ => false
      })

  private def T_True_Premise(v: BooleanConstant) =
    v.constant == "true" &&
      (v.typeAnnotation match {
        case None => true
        case Some(t) => t == TypeInformations.Bool
        case _ => false
      })

}


