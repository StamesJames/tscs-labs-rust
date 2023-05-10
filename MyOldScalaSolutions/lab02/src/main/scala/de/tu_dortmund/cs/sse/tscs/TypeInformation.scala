package de.tu_dortmund.cs.sse.tscs

class TypeInformation(typeInfo : String)

case class BaseTypeInformation(typeInfo : String) extends TypeInformation(typeInfo) {
  override def toString: String = typeInfo
}

object TypeInformations {
  def Nat = new BaseTypeInformation("Nat")
  def Bool = new BaseTypeInformation("Bool")
}
