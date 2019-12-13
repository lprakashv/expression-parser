package com.lprakashv

import com.lprakashv.Value.{Error, Literal}

sealed trait Expression extends Product {
  def eval: Value
}

object Expression {

  case class Fn(name: String, args: Seq[Expression]) extends Expression {
    override def eval: Value = {
      val evaluatedArgs = args.zip(args.map(_.eval))

      val errorOpt = evaluatedArgs.collectFirst {
        case (e, Error(msg)) => Error(s"Error: [$msg] in expression: [ $e ]")
      }

      val literalArgs = evaluatedArgs.map(_._2).map {
        case Literal(d) => d
      }

      errorOpt.getOrElse(
        FunctionStore.getFunction(name) match {
          case Some(f) => Literal(f(literalArgs))
          case None => Error(s"Function '$name' not found!")
        }
      )
    }
  }

  case class Number(value: Double) extends Expression {
    override def eval: Value = Literal(value)
  }

}