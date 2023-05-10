package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, Typecheck,      TypeInformations, TypingException, FunctionTypeInformation}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaExpression,     LambdaAbstraction, LambdaApplication, UntypedLambdaVariable}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional.{ConditionalEvaluation, ConditionalTypeCheck}

import scala.util.{Failure, Success, Try}

/**
  * Defines a type checker for the typed λ calculus
  */
trait TypedLambdaExpression
  extends LambdaExpression
    with Typecheck
    with ConditionalTypeCheck
    with ConditionalEvaluation {

  override def typecheck(): Try[TypeInformation] = typecheck(this, scala.collection.mutable.HashMap())

  override def typecheck(expr: Expression, gamma: scala.collection.mutable.HashMap[Expression, TypeInformation]): Try[TypeInformation] = {
    val result = expr match {
      // TODO: Implement correct type check
      case TypedLambdaVariable(v, None) => Success(gamma.getOrElse(TypedLambdaVariable(v, None), TypeInformations.AbstractBaseType))
      case TypedLambdaVariable(v, Some(t)) => Success(t)
      case LambdaAbstraction(TypedLambdaVariable(v, v_type_option), term) => {
        val new_gamma = gamma.clone(); // clone ich um eventuelle überschreibungen zu verhindern
        v_type_option match {
          case Some(t)  => new_gamma.put(TypedLambdaVariable(v, None), t)
          case None     => new_gamma.put(TypedLambdaVariable(v, None), TypeInformations.AbstractBaseType)
        }
        typecheck(term, new_gamma)
          .map((target_type) => {
              FunctionTypeInformation(v_type_option.getOrElse(TypeInformations.AbstractBaseType), target_type)
            }
          )
      }
      case LambdaApplication(f, a) =>
        typecheck(f, gamma)
          .flatMap((type_f) => typecheck(a, gamma)
            .flatMap((type_a) => type_f match {
                case FunctionTypeInformation(source_type, target_type) if type_a == source_type => Success(target_type)
                case _ => Failure(new TypingException(LambdaApplication(f, a)))
              }
            )
          )
      
      case _
      => super.typecheck(expr, gamma)
    }

    result
  }

}
