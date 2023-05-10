package de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.extensions.records

import de.tu_dortmund.cs.sse.tscs.lambdacalc.typed.{BasicTypeSyntax, TypedLambdaExpression}
import de.tu_dortmund.cs.sse.tscs.{Expression, TypeInformation}
import org.parboiled2.{CharPredicate, Rule1}

trait RecordSyntax extends BasicTypeSyntax{
  override def Term: Rule1[TypedLambdaExpression] = rule {
    RecordProjectionRule | RecordRule | super.Term
  }

  def RecordRule : Rule1[Record] = rule {
    "{"  ~ zeroOrMore(SingleRecord).separatedBy(",")  ~ "}" ~> {
      (singleRecords : Seq[(String, Expression)]) => Record(singleRecords.toMap)
    }
  }

  def SingleRecord : Rule1[(String, Expression)]= rule {
    RecordLabel ~ "=" ~ Term ~> {(k : String, v : Expression) => (k,v)}
  }

  def RecordLabel : Rule1[String] = rule {
    capture(oneOrMore(CharPredicate.Alpha))
  }

  def RecordProjectionRule : Rule1[RecordProjection] = rule {
    "<" ~ Term ~ "." ~ RecordLabel ~ ">" ~> RecordProjection
  }

  override def TypeInfo : Rule1[TypeInformation] = rule {
    RecordTypeRule | super.TypeInfo
  }

  def RecordTypeRule : Rule1[TypeInformation] = rule {
    "{" ~ zeroOrMore(SingleRecordType).separatedBy(",") ~ "}" ~> {
      (singleRecordTypes : Seq[(String, TypeInformation)]) => RecordType(singleRecordTypes.toMap)
    }
  }

  def SingleRecordType : Rule1[(String, TypeInformation)]= rule {
    RecordLabel ~ "=" ~ TypeInfo ~> {(k : String, v : TypeInformation) => (k,v)}
  }
}
