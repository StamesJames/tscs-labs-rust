package de.tu_dortmund.cs.sse.tscs

abstract class TypeInformation(typeInfo : String){
  def contains_type(t:TypeInformation):Boolean;
  def substitute(type_variable: InferedType, type_information: TypeInformation): TypeInformation;
}

// Diese Case Class nutze ich als Typvariable beim Inferieren der Typen. Solche Typen stehen also für irgendwelche beliebigen anderen Typen und können durch weitere Inferierung noch zu anderen typen substituiert werden. 
case class InferedType(type_count: Int) extends TypeInformation("T" + type_count.toString){
  override def toString: String = "T" + type_count.toString;
  def contains_type(t:TypeInformation):Boolean = this == t;
  def substitute(type_variable: InferedType, type_information: TypeInformation): TypeInformation = if (this == type_variable) type_information else this;
}

case class BaseTypeInformation(typeInfo : String) extends TypeInformation(typeInfo) {
  override def toString: String = typeInfo
  def contains_type(t:TypeInformation):Boolean = this == t;
  def substitute(type_variable: InferedType, type_information: TypeInformation): TypeInformation = this;
}


case class FunctionTypeInformation(sourceType : TypeInformation, targetType : TypeInformation)
  extends TypeInformation(sourceType.toString + " -> " + targetType.toString) {
  override def toString: String = "["+ sourceType + "->"+ targetType  + "]"
  def contains_type(t:TypeInformation):Boolean = sourceType.contains_type(t) || targetType.contains_type(t);
  def substitute(type_variable: InferedType, type_information: TypeInformation): TypeInformation = 
    FunctionTypeInformation(sourceType.substitute(type_variable, type_information), targetType.substitute(type_variable, type_information));

}

object TypeInformations {
  def AbstractBaseType = new BaseTypeInformation("A")
  def Nat = new BaseTypeInformation("Nat")
  def Bool = new BaseTypeInformation("Bool")
}
