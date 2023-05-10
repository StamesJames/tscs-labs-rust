package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references

import de.tu_dortmund.cs.sse.tscs.{Expression, Value}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaExpression, LambdaVariable}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing.SequenceEvaluation

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit.{UnitConstant}


trait ReferencesEvaluation extends LambdaExpression with SequenceEvaluation with StatefulEvaluation {
  override def runStep(input: State): State = {
    input match {
      // TODO: Add rules for reference evaluation
      case (Allocation(term), heap) if progressPossible( (term, heap) ) => {
        val new_state = runStep( (term, heap) );
        (Allocation(new_state._1), new_state._2 )
      }
      case (Allocation(term: Value), heap) => ( Location(heap.length), (heap :+ term) )

      case (Assignment(lhs, rhs), heap) if progressPossible((lhs,heap)) => {
        val new_state = runStep( (lhs, heap) );
        (Assignment(new_state._1, rhs), new_state._2 )
      }
      case (Assignment(lhs: Value, rhs), heap) if progressPossible((rhs, heap)) => {
        val new_state = runStep( (rhs, heap) );
        (Assignment(lhs, new_state._1), new_state._2 )
      }
      case (Assignment( Location(pos), rhs: Value ), heap) => (UnitConstant, heap.updated(pos, rhs))

      case (Dereferencing(term), heap) if progressPossible((term, heap)) => {
        val new_state = runStep( (term, heap) );
        (Dereferencing(new_state._1), new_state._2 )
      }
      case (Dereferencing(Location(pos)), heap) => (heap(pos), heap)
      case _ => super.runStep(input)
    }
  }


  override def substitute(f: PartialFunction[LambdaVariable, Expression], term: Expression): Expression = {
    term match {
      case Allocation(t) => Allocation(substitute(f, t))
      case Assignment(lhs, rhs) => Assignment(substitute(f, lhs), substitute(f, rhs))
      case Dereferencing(t) => Dereferencing(substitute(f,t))
      case _ => super.substitute(f, term)
    }
  }
}
