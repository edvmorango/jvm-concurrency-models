package FuturesAndPromises

import java.time.LocalDateTime

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}
import scala.util.control.NonFatal

object FuturesExecution extends App {

  val f: Future[String] = Future {
    println("Future executing")
    Thread.sleep(1000)
    "Return"
  }

  println(f.isCompleted)

  Thread.sleep(2000)

  println(f.isCompleted)
}

object FuturesCallback extends App {

  val f: Future[String] = Future {
    Thread.sleep(1000)
    "Some computation"
  }

  f.foreach { r =>
    println(s"Executing first callback using: ${Thread.currentThread().getName}")
    Thread.sleep(2000)
    println(s"1 - Uppercase ${r.toUpperCase}")
  }

  f.foreach { r =>
    println(s"Executing second callback using: ${Thread.currentThread().getName}")
    Thread.sleep(1000)
    println(s"2 - Length ${r.length}")
  }

  f.foreach { r =>
    println(s"Executing third callback using: ${Thread.currentThread().getName}" )
    println(s"3 - Lowercase ${r.toLowerCase}")
  }

  Thread.sleep(10000)
}

object FuturesFailure extends App {

  val f: Future[Double] = Future { 10/0 }
  /*
  val f = Future { throw new InterruptedException }
  */

  f.foreach{v => println(s"Result: $v")}

  f.failed.foreach{e => println(s"Exception: $e")}

  Thread.sleep(2000)
}

object FuturesFailuresTry extends App {

  val f: Future[Int] = Future { 10/0 }

  f.onComplete { _ match {
    case Success(v) => println(s"Value: $v")
    case Failure(e) => println(s"Exception: $e")
    }
  }

  Thread.sleep(2000)
}

object FuturesNesting extends App {

  case class Token(value: String, expirationDate: LocalDateTime) {
    def isValid = { !value.isEmpty && expirationDate.compareTo(LocalDateTime.now()) >= 0 }
  }

  def getInfo(token: Token): List[String] = {
    println(s"getInfo: ${Thread.currentThread().getName}")
    if(token.isValid)
      List("info1", "info2", "info3")
    else
      Nil
  }

  def getMoreInfo(token: Token, list: List[String]) = {
    println(s"getMoreInfo: ${Thread.currentThread().getName}")
    println("More info")
  }

  val f: Future[Token] = Future { Token("token", LocalDateTime.MAX) }

  f.foreach { token =>

    println(s"f Future: ${Thread.currentThread().getName}")

    val info = getInfo(token)

    Future { getInfo(token) }.foreach { list =>

      println(s"getInfo Future: ${Thread.currentThread().getName}")

      getMoreInfo(token, list)
    }

    println(info)
  }

  println(s"Main: ${Thread.currentThread().getName}")

  Thread.sleep(2000)
}