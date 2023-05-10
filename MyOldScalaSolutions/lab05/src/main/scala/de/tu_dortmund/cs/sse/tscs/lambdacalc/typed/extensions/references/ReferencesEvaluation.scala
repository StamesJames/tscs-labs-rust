package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing.SequenceEvaluation
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit.UnitConstant
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaAbstraction, LambdaApplication, LambdaExpression, LambdaVariable}
import de.tu_dortmund.cs.sse.tscs.{Expression, Value, lambdacalc}

trait ReferencesEvaluation extends LambdaExpression with SequenceEvaluation with StatefulEvaluation {
  override def runStep(input: State): State = {
    input match {
      /* E-App1 */ case (LambdaApplication(f, a), μ) if !isValue(f) && progressPossible((f,μ))  =>
      ((n: State) => (lambdacalc.LambdaApplication(n._1, a), n._2)) (runStep(f, μ))

    /* E-App2 */ case (LambdaApplication(f, a), μ) if isValue(f) && progressPossible((a,μ)) =>
      ((n: State) => (lambdacalc.LambdaApplication(f, n._1), n._2)) (runStep(a, μ))

    /* E-AppAbs */ case (LambdaApplication(LambdaAbstraction(x,t12), v2), μ) if isValue(v2) =>
      (-->(LambdaApplication(LambdaAbstraction(x,t12), v2)), μ)

    /* E-RefV */ case (Allocation(v : Value), μ) => (Location(μ.length), μ :+ v)

    /* E-Ref */ case (Allocation(t), μ) if progressPossible((t, μ)) =>
      ((n : State) => (references.Allocation(n._1), n._2)) (runStep(t, μ))

    /* E-DerefLoc */ case (Dereferencing(Location(l)), μ) if l < μ.length =>
      (μ(l), μ)

    /* E-Deref */ case (Dereferencing(t), μ) if progressPossible((t, μ)) =>
      ((n: State) => (references.Dereferencing(n._1), n._2)) (runStep(t, μ))

    /* E-Assign */ case (Assignment(Location(l), v: Value), μ) =>
      (UnitConstant, μ.patch(l, Seq(v), 1))

    /* E-Assign1 */ case (Assignment(lhs, rhs), μ) if !isValue(lhs) && progressPossible((lhs, μ)) =>
      ((n : State) => (references.Assignment(n._1, rhs), n._2)) (runStep(lhs, μ))

    /* E-Assign2 */ case (Assignment(lhs, rhs), μ) if isValue(lhs) && progressPossible((rhs, μ)) =>
      ((n : State) => (references.Assignment(lhs, n._1), n._2)) (runStep(rhs, μ))

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
