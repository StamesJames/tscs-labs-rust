package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records

import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation, Typecheck}

import scala.util.Try
import scala.util.{Success}

trait RecordTypecheck extends Typecheck {
  override def typecheck(expr: Expression, gamma: Map[Expression, TypeInformation]): Try[TypeInformation] = {
    expr match {
      case Record(kvp) if kvp.forall( {case (k,v) => typecheck(v,gamma).isSuccess} ) 
        => Success(RecordType(kvp.map( {case (k,v) => (k, typecheck(v,gamma).get) } )))
      case RecordProjection(rec @ Record(kvp), projection) if typecheck(rec, gamma).isSuccess && kvp.contains(projection) 
      // ich habe hier jetzt mal noch den typecheck auf rec hinzugefügt. an sich ist der glaube ich nicht unbeding nötig, und in den formalen regeln auch nicht drin aber ich fänd die sprache wäre irgendwie seltsam, wenn das nicht geprüft wird. Auch wenn es ja nicht kaputt gehen sollte, wenn der eintrag in dem Record halt eh nie aufgerufen und somit nicht getypchecked wird. 
        => typecheck( kvp.get(projection).get, gamma)
      case _ => super.typecheck(expr, gamma)
    }
  }
}
