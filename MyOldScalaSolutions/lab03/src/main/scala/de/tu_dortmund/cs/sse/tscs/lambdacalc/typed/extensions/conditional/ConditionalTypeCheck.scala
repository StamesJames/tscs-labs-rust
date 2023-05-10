package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, Typecheck,   TypeInformations, TypingException, FunctionTypeInformation}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaExpression,     LambdaAbstraction, LambdaApplication}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.{TypedLambdaVariable}

import scala.collection.mutable
import scala.util.{Try,    Failure, Success}

trait ConditionalTypeCheck extends Typecheck {
  override def typecheck(): Try[TypeInformation] = typecheck(this,mutable.HashMap())


  override def typecheck(expr: Expression, gamma: mutable.HashMap[Expression, TypeInformation]): Try[TypeInformation] = {
    var result = expr match {
      // TODO: Implement correct type check
      case BooleanConstant("true", x) => Success(TypeInformations.Bool)
      case BooleanConstant("false", x) => Success(TypeInformations.Bool)
      case IfExpr(cond, i_true, i_false) =>
        typecheck(cond, gamma)
          .flatMap((cond_type) => typecheck(i_true, gamma)
            .flatMap((i_true_type) => typecheck(i_false, gamma)
              .flatMap((i_false_type) => if (cond_type == TypeInformations.Bool && i_true_type == i_false_type) Success(i_true_type) else Failure(new TypingException(expr))
              )
            )
          )
      case _ => super.typecheck(expr, gamma)
    }

    result
  }


}
