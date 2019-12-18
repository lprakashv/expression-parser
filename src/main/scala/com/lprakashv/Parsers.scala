package com.lprakashv

import com.lprakashv.Expression.{AddSeqExpression, DivSeqExpression, ExponentExpression, Fn, MulSeqExpression, Number, Parentheses}
import fastparse.MultiLineWhitespace._
import fastparse.{CharIn, P, _}

object Parsers {
  def ws[_: P]: P[Unit] = P(" ".rep(0))

  def number[_: P]: P[Number] = P(
    ("-".rep(min = 0, max = 1) ~ CharIn("0-9").rep(1) ~
      (CharIn(".").rep(min = 0, max = 1) ~ CharIn("0-9").rep(1)).rep(0)).!)
    .map(i => Number(i.toDouble))

  def expressionSequence[_: P]: P[Seq[Expression]] = P(
    ws ~ (ws ~
      additions ~
      ws).rep(sep = ",") ~
      ws
  )

  def function[_: P]: P[Fn] = P(
    ws ~
      CharIn("a-z", "A-Z", "_").rep(1).! ~
      ws ~ "(".rep(exactly = 1) ~ ws ~/
      expressionSequence ~/
      ws ~ ")".rep(exactly = 1) ~
      ws
  ).map {
    case (name, args) => Fn(name, args)
  }

  def parentheses[_: P]: P[Parentheses] = P(
    ws ~
      "(".rep(exactly = 1) ~ ws ~/
      singleTerm ~/
      ws ~ ")".rep(exactly = 1) ~
      ws
  ).map(Parentheses)

  def singleTerm[_: P]: P[SingleTermExpression] = P(parentheses | function | number)

  def multiTermSeqExpression[_: P](binaryOperator: BinaryOperator,
                                   p: => P[Expression]): P[(Expression, Seq[Expression])] = P(
    ws ~ p ~
      (binaryOperator.symbol ~ ws ~/ p ~ ws).rep
      ~ ws
  )

  def exponent[_: P]: P[ExponentExpression] = multiTermSeqExpression(
    BinaryOperator.Pow, singleTerm
  ).map { case (first: Expression, subsequent: Seq[Expression]) =>
    ExponentExpression(first, subsequent.toList)
  }

  def division[_: P]: P[DivSeqExpression] = multiTermSeqExpression(
    BinaryOperator.Divide,
    exponent
  ).map { case (first: Expression, subsequent: Seq[SingleTermExpression]) =>
    DivSeqExpression(first, subsequent.toList)
  }

  def multiplication[_: P]: P[MulSeqExpression] = multiTermSeqExpression(
    BinaryOperator.Multiply,
    division
  ).map { case (first: Expression, subsequent: Seq[SingleTermExpression]) =>
    MulSeqExpression(first, subsequent.toList)
  }

  def additions[_: P]: P[AddSeqExpression] = multiTermSeqExpression(
    BinaryOperator.Plus,
    multiplication
  ).map { case (first: Expression, subsequent: Seq[SingleTermExpression]) =>
    AddSeqExpression(first, subsequent.toList)
  }

  def expression[_: P] = P(ws ~ additions ~ ws ~ End)
}
