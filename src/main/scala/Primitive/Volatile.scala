package Primitive

import java.time.LocalDateTime

object VolatileVar extends App{
  case class Invoice(identifier: Long, store: String, product: String)

  @volatile var ready = false

  val threads = (0 until 5).map { _ =>
    ThreadProducer.thread {
      while(!ready){}

      Thread.sleep(1000)
      println(s"Finishing Thread ${Thread.currentThread().getName} -- ${LocalDateTime.now()}")
    }
  }

  Thread.sleep(1000)
  threads.foreach(t => println(s"${t.getName} --- ${t.getState}"))
  ready = true

}






