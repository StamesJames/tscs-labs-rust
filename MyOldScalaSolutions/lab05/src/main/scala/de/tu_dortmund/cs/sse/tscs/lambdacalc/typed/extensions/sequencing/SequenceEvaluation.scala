package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references.StatefulEvaluation
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit.{UnitConstant, UnitEvaluation}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaExpression, LambdaVariable}
import de.tu_dortmund.cs.sse.tscs.{Expression, Value}

trait SequenceEvaluation extends LambdaExpression with UnitEvaluation with StatefulEvaluation {
  override def runStep(input: (Expression, List[Value])): (Expression, List[Value]) = {
    input match {
      /* E-Seq */ case (Sequence(t1, t2), μ) if progressPossible((t1, μ)) => {
      val firstPartStep = runStep((t1, μ))
      (Sequence(firstPartStep._1, t2), firstPartStep._2)
    }
    /* E-SeqNext */ case(Sequence(UnitConstant, t2),μ) => (t2, μ)
      case _ => super.runStep(input)
    }
  }

  override def -->(expr: Expression): Expression = {
    expr match {
      /* E-Seq */ case Sequence(t1, t2) if progressPossible(t1) => Sequence(-->(t1), t2)
      /* E-SeqNext */ case Sequence(UnitConstant, t2) => t2
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
