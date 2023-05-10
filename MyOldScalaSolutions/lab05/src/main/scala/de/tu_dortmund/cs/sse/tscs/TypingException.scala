package de.tu_dortmund.cs.sse.tscs

class TypingException(failingExpression: Expression) extends RuntimeException("Typing failed for: " + failingExpression.toString())
