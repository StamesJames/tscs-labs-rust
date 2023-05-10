package de.tu_dortmund.cs.sse.tscs.numexpr

import de.tu_dortmund.cs.sse.tscs.Value

abstract class NumericValue(v : String) extends Value(v) with NumericExpression
