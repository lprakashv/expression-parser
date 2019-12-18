package com.lprakashv.models

import com.lprakashv.models.Value.{ErrorValue, Literal}
import com.lprakashv.operators.BinaryOperator

abstract class MultiTermExpression(lhs: Expression,
                                   rhsSeq: List[Expression],
                                   binaryOperator: BinaryOperator,
                                   orderFromLeft: Boolean = true) extends Expression {
  def commonOp(lhs: Value, rhs: Value): Value = (lhs, rhs) match {
    case (e: ErrorValue, _) => e
    case (_, e: ErrorValue) => e
    case (Literal(a), Literal(b)) => Literal(binaryOperator.opEval(a, b))
  }

  override def eval: Value = if (orderFromLeft) {
    (lhs :: rhsSeq).map(_.eval).reduceLeft(commonOp)
  } else {
    (lhs :: rhsSeq).map(_.eval).reduceRight(commonOp)
  }
}

object MultiTermExpression {
  case class ExponentExpression(lhs: Expression, rhsSeq: List[Expression])
    extends MultiTermExpression(lhs, rhsSeq, BinaryOperator.Pow, false)

  case class DivSeqExpression(lhs: Expression, rhsSeq: List[Expression])
    extends MultiTermExpression(lhs, rhsSeq, BinaryOperator.Divide)

  case class MulSeqExpression(lhs: Expression, rhsSeq: List[Expression])
    extends MultiTermExpression(lhs, rhsSeq, BinaryOperator.Multiply)

  case class AddSeqExpression(lhs: Expression, rhsSeq: List[Expression])
    extends MultiTermExpression(lhs, rhsSeq, BinaryOperator.Plus)
}