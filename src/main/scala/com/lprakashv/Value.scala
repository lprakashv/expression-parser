package com.lprakashv

trait Value extends Product

object Value {

  case class Literal(value: Double) extends Value

  case class ErrorValue(msg: String) extends Value

}