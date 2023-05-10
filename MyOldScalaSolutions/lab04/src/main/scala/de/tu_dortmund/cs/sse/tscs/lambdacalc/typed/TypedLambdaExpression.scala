package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed

import de.tu_dortmund.cs.sse.tscs.lambdacalc.LambdaExpression
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional.{ConditionalEvaluation, ConditionalTypeCheck}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.let.{LetEvaluation, LetTypecheck}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references.{ReferencesEvaluation, ReferencesTypecheck}
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing.SequenceTypecheck
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit.UnitTypecheck


/**
  * Defines a type checker for the typed λ calculus
  */
trait TypedLambdaExpression
  extends LambdaExpression
    with BasicTypeCheck
    with ConditionalTypeCheck
    with LetTypecheck
    with ConditionalEvaluation
    with LetEvaluation
    with UnitTypecheck
    with SequenceTypecheck
    with ReferencesEvaluation
    with ReferencesTypecheck

