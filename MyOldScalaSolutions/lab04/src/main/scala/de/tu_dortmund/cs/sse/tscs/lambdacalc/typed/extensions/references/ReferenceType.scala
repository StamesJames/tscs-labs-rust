package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.references

import de.tu_dortmund.cs.sse.tscs.TypeInformation

case class ReferenceType(baseType : TypeInformation) extends TypeInformation(s"Ref ${baseType.toString}" )
