package com.lprakashv.models

import com.lprakashv.models.Value.{ErrorValue, Literal}
import com.lprakashv.operators.FunctionStore

sealed trait SingleTermExpression extends Expression

object SingleTermExpression {

  case class Fn(name: String, args: Seq[Expression]) extends SingleTermExpression {
    override def eval: Value = {
      val evaluatedArgs = args.zip(args.map(_.eval))

      val errorOpt = evaluatedArgs.collectFirst {
        case (e, ErrorValue(msg)) => ErrorValue(s"Error: [$msg] in expression: [ $e ]")
      }

      val literalArgs = evaluatedArgs.map(_._2).collect {
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

  case class Number(value: BigDecimal) extends SingleTermExpression {
    override def eval: Value = Literal(value)
  }

}