package com.lprakashv.parser

import com.lprakashv.models.Value.Literal
import org.scalatest.FunSuite

class StringParserTest extends FunSuite {
  implicit class MathUtils(x: BigDecimal) {
    val precision = 0.001

    def ~=(y: Double) = {
      if ((x.doubleValue - y).abs < precision) true else false
    }

    def ~=(y: Double, precision: Double) = {
      if ((x.doubleValue - y).abs < precision) true else false
    }
  }

  test("sqrt(2^2 + 3^2) == Right(3.6055) (rounded)") {
    assert(
      StringParser
        .parseStringToAST("sqrt(2^2 + 3^2)")
        .map(_.eval match {
          case Literal(value) => value ~= 3.6055
          case _              => false
        })
        .getOrElse(false)
    )
  }

  test("add(10, neg(12), 234.234) == Right(232.234) (rounded)") {
    assert(
      StringParser
        .parseStringToAST("add(10, neg(12), 234.234)")
        .map(_.eval match {
          case Literal(value) => value ~= 232.234
          case _              => false
        })
        .getOrElse(false)
    )
  }

  test("log(square(123.23)) == Right(9.628) (rounded)") {
    assert(
      StringParser
        .parseStringToAST("log(square(123.23))")
        .map(_.eval match {
          case Literal(value) => value ~= 9.628
          case _              => false
        })
        .getOrElse(false)
    )
  }

  test("cube(3) == Right(27) (rounded)") {
    assert(
      StringParser
        .parseStringToAST("cube(3)")
        .map(_.eval match {
          case Literal(value) => value ~= 27
          case _              => false
        })
        .getOrElse(false)
    )
  }

  test("10 - 10 / (6 - 1) - 1 * 1 == Right(7) (rounded)") {
    assert(
      StringParser
        .parseStringToAST("10 - 10 / (6 - 1) - 1 * 1")
        .map(_.eval match {
          case Literal(value) => value ~= 7
          case _              => false
        })
        .getOrElse(false)
    )
  }
}
