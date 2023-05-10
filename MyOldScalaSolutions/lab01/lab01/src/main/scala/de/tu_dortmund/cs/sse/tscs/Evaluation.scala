package de.tu_dortmund.cs.sse.tscs

/**
  * Extends expressions with a framework for small-step semantics style evaluation
  */
trait Evaluation extends Expression{

  def -->(): Expression

  def -->(expr: Expression): Expression

  def progressPossible(expr: Expression) : Boolean = {
    val nextProgression = -->(expr)
    (nextProgression != null)
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
    expr.isInstanceOf[Value]
  }
}
