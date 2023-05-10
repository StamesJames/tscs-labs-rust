package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing

import de.tu_dortmund.cs.sse.tscs.{Expression, Value}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaExpression, LambdaVariable}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references.StatefulEvaluation
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit.{UnitEvaluation, UnitConstant}

trait SequenceEvaluation extends LambdaExpression with UnitEvaluation with StatefulEvaluation {
  override def runStep(input: (Expression, List[Value])): (Expression, List[Value]) = {
    input match {
      // TODO: Add rules for sequence evaluation with a current state
      case (Sequence(UnitConstant, second_term), heap) => runStep( (second_term, heap) )
      case (Sequence(first_term, second_term), heap) if progressPossible((first_term, heap)) =>{
        val new_state = runStep(first_term, heap);
        (Sequence(new_state._1, second_term), new_state._2 )
      }
      case _ => super.runStep(input)
    }
  }

  override def -->(expr: Expression): Expression = {
    expr match {
      // TODO: Add rules for sequence evaluation without a current state
      case Sequence(UnitConstant, second_term) => second_term
      case Sequence(first_term, second_term) if progressPossible(first_term) => Sequence(-->(first_term) , second_term)
      case _ => super.-->(expr)
    }
  }

  override def substitute(f: PartialFunction[LambdaVariable, Expression], term: Expression): Expression = {
    term match {
      /* T-Seq */ case Sequence(t1, t2) => Sequence(substitute(f, t1), substitute(f, t2))
      case _ => super.substitute(f, term)
    }
  }
}
