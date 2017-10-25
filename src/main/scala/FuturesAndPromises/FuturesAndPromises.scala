package FuturesAndPromises

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

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