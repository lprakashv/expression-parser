package com.lprakashv.operators

object FunctionStore {
  private val allFunctions: Map[String, Seq[BigDecimal] => BigDecimal] = Seq(
    ("log", (x: Seq[BigDecimal]) => BigDecimal(math.log(x.head.doubleValue))),
    ("sqrt", (x: Seq[BigDecimal]) => BigDecimal(math.sqrt(x.head.doubleValue))),
    ("square", (x: Seq[BigDecimal]) => x.head * x.head),
    ("cube", (x: Seq[BigDecimal]) => x.head * x.head * x.head),
    ("add", (x: Seq[BigDecimal]) => x.sum),
    ("neg", (x: Seq[BigDecimal]) => BigDecimal(-1) * x.head)
  ).toMap

  def getFunction(name: String): Option[Seq[BigDecimal] => BigDecimal] = allFunctions.get(name)

  def getAllFunctions = allFunctions.keys
}
