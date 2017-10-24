package PrimitiveTechniques

import java.util.concurrent.{ForkJoinPool, TimeUnit}

import scala.collection.parallel
import scala.concurrent.ExecutionContext

object ExecutorExample extends App{

  val executor = new ForkJoinPool()

  executor.execute(new Runnable {
    def run = {
      println("This task is running asynchronously")
      println(s"${Thread.currentThread().getName}")
    }
  })

  Thread.sleep(500)

}

object ExecutorDecoupled extends App{
  val pool = new ForkJoinPool(2)
  val exec = ExecutionContext.fromExecutorService(pool)

  (0 until 10).foreach { _ =>

    exec.execute(new Runnable {
      def run() = {
        println(s"Running ${Thread.currentThread().getName}")
        Thread.sleep(2000)
      }
    })

  }

  exec.awaitTermination(10, TimeUnit.SECONDS)

  println(s"Available Threads ${parallel.availableProcessors} ")

}


