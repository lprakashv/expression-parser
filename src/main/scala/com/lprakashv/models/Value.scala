package com.lprakashv.models

trait Value extends Product

object Value {

  case class Literal(value: BigDecimal) extends Value

  case class ErrorValue(msg: String) extends Value

}
