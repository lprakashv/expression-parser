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

  def multiTermSeqExpressionGeneric[_: P](binaryOperators: Set[BinaryOperator],
                                          overExpression: => P[Expression]
                                         ): P[(Expression, Seq[Expression])] = P(
    ws ~ overExpression ~
      (CharPred(c => binaryOperators.map(_.symbol).contains(c.toString)) ~ ws ~/ overExpression ~ ws).rep
      ~ ws
  )

  def multiTermSeqExpression[_: P](binaryOperator: BinaryOperator,
                                   overExpression: => P[Expression]
                                  ): P[(Expression, Seq[Expression])] =
    multiTermSeqExpressionGeneric(Set(binaryOperator), overExpression)

  def exponent[_: P]: P[ExponentExpression] = multiTermSeqExpression(
    BinaryOperator.Pow, singleTerm
  ).map { case (first: Expression, subsequent: Seq[Expression]) =>
    ExponentExpression(first, subsequent.toList)
  }

  def division[_: P]: P[DivSeqExpression] = multiTermSeqExpression(
    BinaryOperator.Divide,
    exponent
  ).map { case (first: Expression, subsequent: Seq[Expression]) =>
    DivSeqExpression(first, subsequent.toList)
  }

  def multiplication[_: P]: P[MulSeqExpression] = multiTermSeqExpression(
    BinaryOperator.Multiply,
    division
  ).map { case (first: Expression, subsequent: Seq[Expression]) =>
    MulSeqExpression(first, subsequent.toList)
  }

  def additions[_: P]: P[AddSeqExpression] = multiTermSeqExpressionGeneric(
    Set(BinaryOperator.Plus, BinaryOperator.Minus),
    multiplication
  ).map { case (first: Expression, subsequent: Seq[Expression]) =>
    AddSeqExpression(first, subsequent.toList)
  }

  def expression[_: P] = P(ws ~ additions ~ ws ~ End)
}
