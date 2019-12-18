package com.lprakashv.operators

sealed trait BinaryOperator {
  def symbol: String

  def opEval: (BigDecimal, BigDecimal) => BigDecimal
}

object BinaryOperator {

  case object Plus extends BinaryOperator {
    override def symbol: String = "+"

    override def opEval: (BigDecimal, BigDecimal) => BigDecimal = _ + _
  }

  case object Divide extends BinaryOperator {
    override def symbol: String = "/"

    override def opEval: (BigDecimal, BigDecimal) => BigDecimal = _ / _
  }

  case object Multiply extends BinaryOperator {
    override def symbol: String = "*"

    override def opEval: (BigDecimal, BigDecimal) => BigDecimal = _ * _
  }

  case object Minus extends BinaryOperator {
    override def symbol: String = "-"

    override def opEval: (BigDecimal, BigDecimal) => BigDecimal = _ - _
  }

  case object Pow extends BinaryOperator {
    override def symbol: String = "^"

    override def opEval: (BigDecimal, BigDecimal) => BigDecimal =
      (a, b) => BigDecimal(math.pow(a.doubleValue, b.doubleValue))
  }
}