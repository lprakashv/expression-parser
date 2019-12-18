package com.lprakashv

sealed trait BinaryOperator {
  def symbol: String

  //def priority: Int

  def opEval: (Double, Double) => Double
}

object BinaryOperator {

  case object Plus extends BinaryOperator {
    override def symbol: String = "+"

    //override def priority: Int = 3

    override def opEval: (Double, Double) => Double = _ + _
  }

  case object Divide extends BinaryOperator {
    override def symbol: String = "/"

    //override def priority: Int = 1

    override def opEval: (Double, Double) => Double = _ / _
  }

  case object Multiply extends BinaryOperator {
    override def symbol: String = "*"

    //override def priority: Int = 2

    override def opEval: (Double, Double) => Double = _ * _
  }

  case object Minus extends BinaryOperator {
    override def symbol: String = "-"

    //override def priority: Int = 4

    override def opEval: (Double, Double) => Double = _ - _
  }

  case object Pow extends BinaryOperator {
    override def symbol: String = "^"

    //override def priority: Int = 0

    override def opEval: (Double, Double) => Double = math.pow
  }
}