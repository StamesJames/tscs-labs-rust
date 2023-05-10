package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed

import de.tu_dortmund.cs.sse.tscs.lambdacalc.LambdaCalculusSyntax
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional.ConditionalSyntax
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.let.LetSyntax
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records.RecordSyntax
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references.ReferencesSyntax
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.sequencing.SequencingSyntax
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.unit.UnitSyntax
import org.parboiled2.ParserInput

/**
  * A syntax definition for the typed λ calculus. The different syntax extensions are mixed in as traits to keep them
  * as separate as possible.
  */
class TypedLambdaCalculusSyntax(input : ParserInput)
  extends LambdaCalculusSyntax(input)
    with ReferencesSyntax
    with UnitSyntax
    with SequencingSyntax
    with ConditionalSyntax
    with LetSyntax
    with RecordSyntax

