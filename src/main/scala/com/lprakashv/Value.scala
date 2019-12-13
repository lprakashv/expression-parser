package com.lprakashv

trait Value extends Product

object Value {

  case class Literal(value: Double) extends Value

  case class Error(msg: String) extends Value

}