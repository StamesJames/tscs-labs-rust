package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed

import de.tu_dortmund.cs.sse.tscs.lambdacalc.LambdaCalculusSyntax
import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.conditional.ConditionalSyntax
import org.parboiled2.ParserInput

/**
  * A syntax definition for the typed λ calculus. The different syntax extensions are mixed in as traits to keep them
  * as separate as possible.
  */
class TypedLambdaCalculusSyntax(input : ParserInput)
  extends LambdaCalculusSyntax(input)
    with BasicTypeSyntax
    with ConditionalSyntax

