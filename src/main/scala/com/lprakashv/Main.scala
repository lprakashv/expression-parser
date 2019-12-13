package com.lprakashv

import com.lprakashv.Value.{Error, Literal}
import fastparse.parse

object Main extends App {
  var keepReading = true

  println(s"All available functions:\n${FunctionStore.getAllFunctions.mkString(", ")}")

  while (keepReading) {
    print("Enter expression ('quit' | 'exit' to quit) |> ")

    val line = scala.io.StdIn.readLine()

    if (line == "quit" || line == "exit") {
      keepReading = false
      println("Quitting...")
    } else {
      parse(line, Parsers.expr(_).map(_.eval)).fold(
        (f0, f1, f2) => println(s"Error : [$f0] [$f1] [$f2]"),
        (s0, _) => s0 match {
          case Literal(value) => println(s"Value : $value")
          case Error(msg) => println(s"$msg")
        }
      )
    }
  }
}
