package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, Typecheck,      TypeInformations, TypingException, FunctionTypeInformation, TypeInferer}
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
    if (gamma.contains(expr)) {return Success(gamma.get(expr).get)}
    val result = expr match {
      // TODO: Implement correct type check
      case TypedLambdaVariable(v, None) => Success(gamma.getOrElse(UntypedLambdaVariable(v), TypeInferer.next_type))
      case TypedLambdaVariable(v, Some(t)) => { gamma.put(UntypedLambdaVariable(v), t); Success(t)}
      case LambdaAbstraction(TypedLambdaVariable(v, v_type_option), term) => {
        var new_gamma = gamma.clone(); // clone ich um eventuelle überschreibungen zu verhindern.
        v_type_option match {
          case Some(t)  => new_gamma.put(UntypedLambdaVariable(v), t)
          case None     => new_gamma.put(UntypedLambdaVariable(v), TypeInferer.next_type)
        }
        typecheck(term, new_gamma)
          .map((target_type) => {
              FunctionTypeInformation(new_gamma.get(UntypedLambdaVariable(v)).get, target_type)
            }
          )
      }
      case LambdaApplication(f, a) =>
        typecheck(f, gamma)
          .flatMap((type_f) => typecheck(a, gamma)
            .flatMap((type_a) => {
              var target_type = TypeInferer.next_type;
              if (TypeInferer.unifificate(type_f, FunctionTypeInformation(type_a, target_type), gamma)) {
                gamma.get(f).get match {
                  case FunctionTypeInformation(s, t) => Success(t)
                  case _ => Failure( new TypingException(expr))
                }
              } else {
                Failure( new TypingException(expr))
              }
            }
            )
          )
      
      case _
      => super.typecheck(expr, gamma)
    };
    result match {
      case Success(t) => gamma.put(expr, t)
      case _ => ()
    };
    result
  }

}
