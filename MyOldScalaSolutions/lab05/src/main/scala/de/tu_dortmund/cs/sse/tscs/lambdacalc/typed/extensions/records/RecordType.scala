package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records

import de.tu_dortmund.cs.sse.tscs.TypeInformation

case class RecordType(types : Map[String, TypeInformation]) extends TypeInformation(s"{${types.values.mkString(",")}}")
