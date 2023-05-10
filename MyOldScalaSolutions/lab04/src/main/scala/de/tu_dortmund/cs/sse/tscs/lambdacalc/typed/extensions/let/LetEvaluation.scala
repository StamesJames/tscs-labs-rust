package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.let

import de.tu_dortmund.cs.sse.tscs.{Expression, Value}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.{LambdaExpression, LambdaVariable, UntypedLambdaVariable}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references.StatefulEvaluation

trait LetEvaluation extends LambdaExpression with StatefulEvaluation {
  override def runStep(input: (Expression, List[Value])): (Expression, List[Value]) = {
    input match {
      // TODO: Add rules for let evaluation with a current state
      case ( Let(name : LambdaVariable, substitutionTerm, innerTerm), heap) if progressPossible((substitutionTerm, heap)) 
      => {
        val new_state = runStep( (substitutionTerm, heap) );
        (Let(name, new_state._1, innerTerm), new_state._2 )
      }
      case ( Let(name : LambdaVariable, substitutionTerm: Value, innerTerm), heap)
      => (substitute( {case `name` => substitutionTerm}, innerTerm ), heap)
      case _ => super.runStep(input)
    }
  }

  override def -->(expr: Expression): Expression = {
    expr match {
      case Let(name:LambdaVariable, substitutionTerm, innerTerm) if progressPossible(substitutionTerm) => Let(name, -->(substitutionTerm), innerTerm)
      case Let(name:LambdaVariable, substitutionTerm, innerTerm) => substitute( {case `name` => substitutionTerm}, innerTerm )
      case _ => super.-->(expr)
    }
  }

  override def substitute(f: PartialFunction[LambdaVariable, Expression], term: Expression): Expression = {
    term match {
      case Let(variable, substitutionTerm, innerTerm) => Let(variable, substitute(f, substitutionTerm), substitute(f, innerTerm))
      case _ => super.substitute(f, term)
    }
  }
}
