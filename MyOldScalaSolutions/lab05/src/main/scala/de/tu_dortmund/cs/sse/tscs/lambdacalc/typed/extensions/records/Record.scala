package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.TypedLambdaExpression
import de.tu_dortmund.cs.sse.tscs.{Expression, Value}

case class Record(kvp : Map[String, Expression])
  extends Value(s"{${kvp.map(x => s"${x._1}:${x._2}").mkString(",")}}")
    with TypedLambdaExpression {

  def isFullValueRec : Boolean = {
    getFirstNonValueEntry.isEmpty
  }

   def getFirstNonValueEntry : Option[(String, Expression)] = {
    kvp.find(pair => !pair._2.isInstanceOf[Value])
  }
}