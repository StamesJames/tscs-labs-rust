package de.tu_dortmund.cs.sse.tscs

class TypeInformation(typeInfo : String)

case class BaseTypeInformation(typeInfo : String) extends TypeInformation(typeInfo) {
  override def toString: String = typeInfo
}

case class FunctionTypeInformation(sourceType : TypeInformation, targetType : TypeInformation)
  extends TypeInformation(sourceType.toString + " -> " + targetType.toString) {
  override def toString: String = "["+ sourceType + "->"+ targetType  + "]"
}


object TypeInformations {
  def AbstractBaseType = new BaseTypeInformation("A")
  def Nat = new BaseTypeInformation("Nat")
  def Bool = new BaseTypeInformation("Bool")
}
