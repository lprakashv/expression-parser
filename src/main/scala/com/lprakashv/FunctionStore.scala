package com.lprakashv

object FunctionStore {
  private val allFunctions: Map[String, Seq[Double] => Double] = Seq(
    ("log", (x: Seq[Double]) => math.log(x.head)),
    ("sqrt", (x: Seq[Double]) => math.sqrt(x.head)),
    ("square", (x: Seq[Double]) => x.head * x.head),
    ("cube", (x: Seq[Double]) => x.head * x.head * x.head),
    ("add", (x: Seq[Double]) => x.sum),
    ("neg", (x: Seq[Double]) => -1 * x.head)
  ).toMap

  def getFunction(name: String): Option[Seq[Double] => Double] = allFunctions.get(name)

  def getAllFunctions = allFunctions.keys
}
