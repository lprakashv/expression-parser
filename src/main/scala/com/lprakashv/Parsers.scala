package com.lprakashv

import com.lprakashv.Expression.{Fn, Number}
import fastparse.MultiLineWhitespace._
import fastparse.{CharIn, P, _}

object Parsers {
  def ws[_: P] = P(" ".rep(0))

  def number[_: P]: P[Number] = P(
    (CharIn("0-9").rep(1) ~
      (CharIn(".").rep(min = 0, max = 1) ~ CharIn("0-9").rep(1)).rep(0)).!)
    .map(i => Number(i.toDouble))

  def seq[_: P]: P[Seq[Expression]] = P(
    ws ~ (ws ~
      expr ~
      ws).rep(sep=",") ~
      ws
  )

  def fn[_: P]: P[Fn] = P(
    ws ~
      CharIn("a-z", "A-Z", "_").rep(1).! ~
      ws ~ "(".rep(exactly=1) ~ ws ~/
      seq ~/
      ws ~ ")".rep(exactly=1) ~
      ws
  ).map {
    case (name, args) => Fn(name, args)
  }

  def expr[_: P]: P[Expression] = P(fn | number)
}
