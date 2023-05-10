package de.tu_dortmund.cs.sse.tscs

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records.Record

/**
  * Extends expressions with a framework for small-step semantics style evaluation
  */
trait Evaluation extends Expression {

  def -->(): Expression

  def -->(expr: Expression): Expression = null

  def progressPossible(expr: Expression) : Boolean = {
    val nextProgression = -->(expr)
    (nextProgression != null && nextProgression != expr)
  }

  def isValue(expr: Expression) : Boolean = expr.isInstanceOf[Value]

  def -->*() : Expression = -->*(this)

  def -->*(input: Expression): Expression = {
    var expr = -->(input)

    while (true) {
      if (isFinalResult(expr)) return expr
      if (expr == null) {
        println("Term is stuck")
        return null
      }
      expr = -->(expr)
    }
    return null
  }



  def isFinalResult(expr: Expression) : Boolean = {
    expr match {
      case r: Record => r.isFullValueRec
      case _ => expr.isInstanceOf[Value]
    }
  }
}
