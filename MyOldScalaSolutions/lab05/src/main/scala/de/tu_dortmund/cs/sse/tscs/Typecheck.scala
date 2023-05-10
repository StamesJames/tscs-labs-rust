package de.tu_dortmund.cs.sse.tscs

import scala.collection.immutable
import scala.util.{Failure, Success, Try}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records.{RecordType}


/**
  * Basic trait for type checking
  */
trait Typecheck extends Expression {

  def typecheck() : Try[TypeInformation] = typecheck(this, immutable.HashMap())

  def typecheck(expr: Expression, gamma: immutable.Map[Expression, TypeInformation]): Try[TypeInformation] = {
    println("Failure bounce for " + expr)
    Failure(new TypingException(expr))
  }

  def is_subtype_of(lhs:TypeInformation, rhs: TypeInformation): Boolean = {
    if (lhs == rhs) return true;
    if (rhs == TypeInformations.Top) return true;
    lhs match {
      case RecordType(kvp_lhs) => rhs match {
        case RecordType(kvp_rhs) => kvp_rhs.forall({ case (k,v) => kvp_lhs.contains(k) && is_subtype_of(kvp_lhs.get(k).get, v)})
        case _ => false
      }
      // case FunctionType(source_lhs, target_lhs) => rhs match {
      //   case FunctionType(source_rhs, target_rhs) => is_subtype_of(source_rhs, source_lhs) && is_subtype_of(target_lhs, target_rhs)
      //   case _ => false
      // }
      case _ => false
    }
  }

  def join(type_1:TypeInformation, type_2: TypeInformation):TypeInformation = {
    if (type_1 == type_2) return type_1;
    type_1 match {
      case RecordType(kvp_1) => type_2 match{
        case RecordType(kvp_2) 
          => RecordType(kvp_1.foldLeft[Map[String, TypeInformation]](Map())( 
              {case (map, (k, v)) 
                =>  if (kvp_2.contains(k)) {
                      map ++ Array( (k, join(v, kvp_2.get(k).get)))
                    } else {
                      map
                    } 
              }))
        case _ => TypeInformations.Top
      }
      case _ => TypeInformations.Top
    }

  }

}
