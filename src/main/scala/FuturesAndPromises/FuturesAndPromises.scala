package FuturesAndPromises

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FuturesExample extends App{


  val f : Future[String] = Future{
    println("Future executing")
    Thread.sleep(1000)
    "Return"
  }


  println(f.isCompleted)

  Thread.sleep(2000)





}