package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references

import de.tu_dortmund.cs.sse.tscs.{Evaluation, Expression, Value}

trait StatefulEvaluation extends Evaluation {
  type State = (Expression, List[Value])
  val initial: State = (this : Expression, List[Value]())

  def runStep() : State = runStep(initial)
  def runStep(input : State): State = null

  def runFull() : State = runFull(initial)
  def runFull(input : State) : State = {
      var currentState = runStep(input)


      while (true) {
        if (currentState == null) {
          println("Term is stuck")
          return null
        }
        if (isFinalResult(currentState._1)) return currentState

        currentState = runStep(currentState)
      }
      return null
  }


  def progressPossible(input : State): Boolean = {
    val nextProgression = runStep(input)
    (nextProgression != null && nextProgression._1 != input._1)
  }

}
