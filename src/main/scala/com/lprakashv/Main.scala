package com.lprakashv

import com.lprakashv.Value.{ErrorValue, Literal}
import fastparse.parse

object Main extends App {
  var keepReading = true

  println(s"All available functions:\n${FunctionStore.getAllFunctions.mkString(", ")}")

  while (keepReading) {
    print("Expression=> ")

    val line = scala.io.StdIn.readLine()

    if (line == "quit" || line == "exit") {
      keepReading = false
      println("Quitting...")
    } else {
      parse(
        line,
        Parsers.expression(_).map(_.eval)
      ).fold(
        (f0, f1, f2) => println(s"Error at '${line.charAt(f1)}' [ internal message: $f2 ]"),
        (s0, _) => s0 match {
          case Literal(value) => println(s"Value : $value")
          case ErrorValue(msg) => println(s"$msg")
        }
      )
    }
  }
}
