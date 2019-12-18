package com.lprakashv.parsers

import com.lprakashv.models.MultiTermExpression.{AddSeqExpression, DivSeqExpression, ExponentExpression, MulSeqExpression}
import com.lprakashv.models.SingleTermExpression.{Fn, Number, Parentheses}
import com.lprakashv.models.{Expression, SingleTermExpression}
import com.lprakashv.operators.BinaryOperator
import fastparse.MultiLineWhitespace._
import fastparse.{CharIn, CharPred, P, _}

object Parsers {
  // whitespaces
  def ws[_: P]: P[Unit] = P(" ".rep(0))

  def number[_: P]: P[Number] = P(
    (
      // zero of one times '-' before number
      "-".rep(min = 0, max = 1) ~
        (
          CharIn("0-9").rep(1) ~
            (
              // decimal: exactly one '.' followed by sequence of digits
              CharIn(".").rep(exactly = 1) ~
                CharIn("0-9").rep(1)
              ).rep(0, max = 1)
          )
      ).!
  ).map(i => Number(BigDecimal(i)))

  def expressionSequence[_: P]: P[Seq[Expression]] = P(
    ws ~
      (
        ws ~
          additions ~
          ws
        ).rep(sep = ",") ~
      ws
  )

  def function[_: P]: P[Fn] = P(
    ws ~
      CharIn("a-z", "A-Z", "_").rep(1).! ~
      ws ~ "(".rep(exactly = 1) ~ ws ~
      expressionSequence ~
      ws ~ ")".rep(exactly = 1) ~
      ws
  ).map {
    case (name, args) => Fn(name, args)
  }

  def parentheses[_: P]: P[Parentheses] = P(
    ws ~
      "(".rep(exactly = 1) ~ ws ~
      additions ~
      ws ~ ")".rep(exactly = 1) ~
      ws
  ).map(Parentheses)

  // single unit of expression
  def singleTerm[_: P]: P[SingleTermExpression] = P(parentheses | function | number)

  // clubbed operations can be considered a single unit (`2 * 5 * 23`, `2 ^ 1 ^ 4` etc)
  def multiTermSeqExpressionGeneric[_: P](binaryOperators: Set[BinaryOperator],
                                          overExpression: => P[Expression]
                                         ): P[(Expression, Seq[(String, Expression)])] = P(
    ws ~ overExpression ~ ws ~
      (ws ~ CharPred(c => binaryOperators.exists(_.symbol == c.toString)).! ~ ws ~ overExpression ~ ws).rep
      ~ ws
  )

  // multiTermSeqExpressionGeneric with just a single operator
  def multiTermSeqExpression[_: P](binaryOperator: BinaryOperator,
                                   overExpression: => P[Expression]
                                  ): P[(Expression, Seq[Expression])] =
    multiTermSeqExpressionGeneric(Set(binaryOperator), overExpression).map {
      case (first, operationsWithOperator) => (first, operationsWithOperator.map(_._2))
    }

  // multiTermSeqExpression with '^' as operator
  def exponent[_: P]: P[ExponentExpression] = multiTermSeqExpression(
    BinaryOperator.Pow,
    singleTerm
  ).map { case (first: Expression, subsequent: Seq[Expression]) =>
    ExponentExpression(first, subsequent.toList)
  }

  // multiTermSeqExpression with '/' as operator
  def division[_: P]: P[DivSeqExpression] = multiTermSeqExpression(
    BinaryOperator.Divide,
    exponent
  ).map { case (first: Expression, subsequent: Seq[Expression]) =>
    DivSeqExpression(first, subsequent.toList)
  }

  // multiTermSeqExpression with '*' as operator
  def multiplication[_: P]: P[MulSeqExpression] = multiTermSeqExpression(
    BinaryOperator.Multiply,
    division
  ).map { case (first: Expression, subsequent: Seq[Expression]) =>
    MulSeqExpression(first, subsequent.toList)
  }

  // multiTermSeqExpressionGeneric with '+' and '-' as operators
  def additions[_: P]: P[AddSeqExpression] = multiTermSeqExpressionGeneric(
    Set(BinaryOperator.Plus, BinaryOperator.Minus),
    multiplication
  ).map { case (first: Expression, subsequent: Seq[(String, Expression)]) =>
    val onlyAdditionSubsequent: Seq[Expression] = subsequent.map {
      case ("-", expr) => MulSeqExpression(Number(-1), List(expr))
      case ("+", expr) => expr
      case (o, expr) => throw new RuntimeException(s"Invalid operation $o on $expr")
    }
    AddSeqExpression(first, onlyAdditionSubsequent.toList)
  }

  // an expression can be considered addition of multiple single units
  // example : ((2 * 3) + (4 ^ (2 * 5)) + (5 / 2))
  def expression[_: P]: P[AddSeqExpression] = P(ws ~ additions ~ ws ~ End)
}
