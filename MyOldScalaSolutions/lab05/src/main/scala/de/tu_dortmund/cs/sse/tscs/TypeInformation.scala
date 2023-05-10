package de.tu_dortmund.cs.sse.tscs

class TypeInformation(typeInfo : String)

case class BaseTypeInformation(typeInfo : String) extends TypeInformation(typeInfo) {
  override def toString: String = typeInfo
}

case class FunctionType(sourceType : TypeInformation, targetType : TypeInformation)
  extends TypeInformation(sourceType.toString + " -> " + targetType.toString) {
  override def toString: String = "["+ sourceType + "->"+ targetType  + "]"
}


object TypeInformations {
  val AbstractBaseType = new BaseTypeInformation( "A")
  val Nat = new BaseTypeInformation("Nat")
  val Bool = new BaseTypeInformation("Bool")
  val Top = new BaseTypeInformation("Top")
}
