package com.lprakashv.parser

import com.lprakashv.models.MultiTermExpression.AddSeqExpression
import fastparse.parse

object StringParser {

  def parseStringToAST(line: String): Either[String, AddSeqExpression] = {
    parse(
      line,
      Parsers.expression(_),
      verboseFailures = true
    ).fold(
      (_, f1, f2) => {
        println(s"${line.take(f1)}'${line.charAt(f1)}'${line.substring(f1 + 1)}")
        println(s"${(List.fill(f1 + 1)(" ") ++ List("^")).mkString}")

        Left(f2.toString)
      },
      (s0, _) => Right(s0)
    )
  }
}
