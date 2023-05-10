package de.tu_dortmund.cs.sse.tscs

/**
  * Representation of a value
  *
  * @param value the value to be represented
  */
class Value(value: String) extends Expression {
  override def toString: String = value
}
