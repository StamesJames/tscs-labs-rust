package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references.StatefulEvaluation
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaExpression, LambdaVariable}
import de.tu_dortmund.cs.sse.tscs.{Expression, Value}

trait RecordEvaluation extends LambdaExpression with StatefulEvaluation {



  override def -->(expr: Expression): Expression = {
    expr match {

        /* Record Values */
      case rec: Record if rec.isFullValueRec
        => rec
      case rec @ Record(kvp) if !rec.isFullValueRec 
        => Record(kvp ++ Array(rec.getFirstNonValueEntry.map( {case (k,v) => (k, -->(v)) } ).get) )
      case RecordProjection(rec @ Record(kvp), projection) if !rec.isFullValueRec
        => RecordProjection(-->(rec), projection)
      case RecordProjection(rec @ Record(kvp), projection) if rec.isFullValueRec && kvp.contains(projection)
        => kvp.get(projection).get
      case _ => super.-->(expr)
    }
  }

  override def runStep(input: (Expression, List[Value])): (Expression, List[Value]) = {
    input match {

      /* Record Values */
      case (rec: Record, µ)
        => (rec, µ)

      case _ => super.runStep(input)
    }
  }

  override def substitute(f: PartialFunction[LambdaVariable, Expression], term: Expression): Expression = {
    term match {
      case RecordProjection(rec, projection) => records.RecordProjection(substitute(f, rec), projection)
      case Record(kvp) => Record(kvp.map(entry => (entry._1, substitute(f, entry._2))))
      case _ => super.substitute(f, term)
    }

  }
}
