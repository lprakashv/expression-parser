package com.lprakashv

import com.lprakashv.models.Value.{ErrorValue, Literal}
import com.lprakashv.operators.FunctionStore
import com.lprakashv.parsers.Parsers
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
      try {
        parse(
          line,
          Parsers.expression(_).map(_.eval),
          verboseFailures = true
        ).fold(
          (_, f1, f2) => {
            println(s"${line.take(f1)}'${line.charAt(f1)}'${line.substring(f1+1)}")
            println(s"${(List.fill(f1+1)(" ") ++ List("^")).mkString}")
            println(s"Internal Message: $f2")
          },
          (s0, _) => s0 match {
            case Literal(value) => println(s"Value : $value")
            case ErrorValue(msg) => println(s"$msg")
          }
        )
      } catch {
        case e: Throwable => println(s"Exception occurred: ${e.getLocalizedMessage}")
      }
    }
  }
}
