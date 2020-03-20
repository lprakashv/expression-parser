package com.lprakashv

import com.lprakashv.models.Value.{ErrorValue, Literal}
import com.lprakashv.operators.FunctionStore
import com.lprakashv.parser.StringParser

object Repl extends App {
  var keepReading = true

  println(
    s"All available functions:\n${FunctionStore.getAllFunctions.mkString(", ")}"
  )

  while (keepReading) {
    print("Expression=> ")

    val line = scala.io.StdIn.readLine()

    if (line == "quit" || line == "exit") {
      keepReading = false
      println("Quitting...")
    } else {
      try {
        StringParser.parseStringToAST(line) match {
          case Left(error) => println(error)
          case Right(ast) =>
            ast.eval match {
              case Literal(value)  => println(s"Value : $value")
              case ErrorValue(msg) => println(msg)
            }
        }
      } catch {
        case e: Throwable =>
          println(s"Exception occurred: ${e.getLocalizedMessage}")
      }
    }
  }
}
