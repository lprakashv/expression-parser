package com.lprakashv

import com.lprakashv.Value.{ErrorValue, Literal}

sealed trait Expression extends Product {
  def eval: Value
}

sealed trait SingleTermExpression extends Expression

object Expression {

  case class Fn(name: String, args: Seq[Expression]) extends SingleTermExpression {
    override def eval: Value = {
      val evaluatedArgs = args.zip(args.map(_.eval))

      val errorOpt = evaluatedArgs.collectFirst {
        case (e, ErrorValue(msg)) => ErrorValue(s"Error: [$msg] in expression: [ $e ]")
      }

      val literalArgs = evaluatedArgs.map(_._2).map {
        case Literal(d) => d
      }

      errorOpt.getOrElse(
        FunctionStore.getFunction(name) match {
          case Some(f) => Literal(f(literalArgs))
          case None => ErrorValue(s"Function '$name' not found!")
        }
      )
    }
  }

  case class Parentheses(expr: Expression) extends SingleTermExpression {
    override def eval: Value = expr.eval
  }

  case class Number(value: Double) extends SingleTermExpression {
    override def eval: Value = Literal(value)
  }

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

  case class ExponentExpression(lhs: Expression, rhsSeq: List[Expression])
    extends MultiTermExpression(lhs, rhsSeq, BinaryOperator.Pow, false)

  case class DivSeqExpression(lhs: Expression, rhsSeq: List[Expression])
    extends MultiTermExpression(lhs, rhsSeq, BinaryOperator.Divide)

  case class MulSeqExpression(lhs: Expression, rhsSeq: List[Expression])
    extends MultiTermExpression(lhs, rhsSeq, BinaryOperator.Multiply)

  case class AddSeqExpression(lhs: Expression, rhsSeq: List[Expression])
    extends MultiTermExpression(lhs, rhsSeq, BinaryOperator.Plus)

}