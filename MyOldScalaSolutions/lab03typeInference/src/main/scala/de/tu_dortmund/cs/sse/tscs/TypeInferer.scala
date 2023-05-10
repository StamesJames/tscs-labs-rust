package de.tu_dortmund.cs.sse.tscs

import scala.collection.mutable


object TypeInferer{
    var type_count: Int = 0;

    // Gibt den nächsten noch ungenutzte Typvarable vom Typ InferedType zurück. Ich nutze hier einfach eine aufsteigende Zählung was allerdings am ende in den Ergebnissen seltsam ist, wenn zuvor schon viele typvariablen genutzt werden
    def next_type():InferedType = {
        this.type_count += 1;
        InferedType(type_count)
    }

    // Versucht zwei typen zu unifizieren und gibt im falle eines erfolges true und ansonsten false zurück. Konnte unifiziert werden, werden die dazu nötigen substitutionen von Typvariablen in gamma durchgefürht
    def unifificate(type_a: TypeInformation, type_b: TypeInformation, gamma: mutable.HashMap[Expression, TypeInformation]): Boolean = {
        if (type_a == type_b) return true;
        type_a match {
            case InferedType(x) => if (!type_b.contains_type(InferedType(x))) this.substitute(InferedType(x), type_b, gamma) else false
            case BaseTypeInformation(s) => type_b match {
                case InferedType(x) => this.substitute(InferedType(x), type_a, gamma)
                case _ => false
            }
            case FunctionTypeInformation(source_type_a, target_type_a) => type_b match {
                case InferedType(x) => if (!type_a.contains_type(InferedType(x))) this.substitute(InferedType(x), type_b, gamma) else false
                case FunctionTypeInformation(source_type_b, target_type_b) => this.unifificate(source_type_a, source_type_b, gamma) && this.unifificate(target_type_a, target_type_b, gamma)
                case _ => false
            }
        }
    }

    // Führt eine gewünschte Substitution von einer Typvariablen hin zu einerm anderen Typ in gamma durch. 
    def substitute(type_variable: InferedType, type_information: TypeInformation, gamma: mutable.HashMap[Expression, TypeInformation]): Boolean ={
        gamma.mapValuesInPlace( (key, value) => value.substitute(type_variable, type_information) )
        return true
    }
}