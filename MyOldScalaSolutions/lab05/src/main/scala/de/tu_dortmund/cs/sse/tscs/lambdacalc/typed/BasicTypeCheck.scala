package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed

import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaAbstraction, LambdaApplication}
import de.tu_dortmund.cs.sse.tscs._

import scala.util.{Success, Try}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records.{RecordType}


trait BasicTypeCheck extends Typecheck {
  override def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation]): Try[TypeInformation] = {
    val result = expr match {

      /* T-Var */
      // If variables are already known in the context, then we can successfully type them
      case v: TypedLambdaVariable if v.typeAnnotation.isEmpty && gamma.contains(v)
      => Success(gamma.get(v).get)
      // Assume abstract base type for variables
      case v: TypedLambdaVariable if v.typeAnnotation.isEmpty
      => Success(TypeInformations.AbstractBaseType)
      // Or simply trust the annotation
      case v: TypedLambdaVariable if v.typeAnnotation.isDefined
      => Success(v.typeAnnotation.get)

      /* T-Abs */
      case abs: LambdaAbstraction
        if typecheck(abs.variable, gamma).isSuccess && typecheck(abs.term, gamma + (abs.variable -> typecheck(abs.variable, gamma).get)).isSuccess
      => Success(FunctionType(typecheck(abs.variable, gamma).get, typecheck(abs.term, gamma + (abs.variable -> typecheck(abs.variable, gamma).get)).get))

      /* T-App */
      case app: LambdaApplication if T_App_Premise(app, gamma)
      => Success(typecheck(app.function, gamma).get.asInstanceOf[FunctionType].targetType)

      case _
      => super.typecheck(expr, gamma)
    }

    /*
    print(expr.toString + " --- ")
    println(result match {
      case Success(t) => "Successfully typed to " + t
      case Failure(e) => "Typing failed"
    })*/

    result
  }


  private def T_App_Premise(app: LambdaApplication, gamma: Map[Expression, TypeInformation]) =
    (typecheck(app.function, gamma), typecheck(app.argument, gamma)) match {
      case (Success(ft: FunctionType), Success(at)) => is_subtype_of(at, ft.sourceType)
      case e => false
  }

}
