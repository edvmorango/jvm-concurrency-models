import scala.annotation.tailrec
import scala.collection._

object ThreadPoolExpensive extends App {
  val tasks = mutable.Queue[() => Unit]()


  object Worker extends Thread {

    setDaemon(true)

    def pool(): Option[() => Unit] = tasks.synchronized {
      if(tasks.nonEmpty)
        Some(tasks.dequeue())
      else
        None
    }

    override def run() = while (true) { pool().map(_()) }



  }

  Worker.start()

  def asynchronous(body: => Unit) = tasks.enqueue( () => body )

  asynchronous{ println("1") }
  asynchronous{ println("2") }
  asynchronous{ println("3") }
  asynchronous{ println("4") }

  Thread.sleep(1000)
  println(Worker.getState)
  Thread.sleep(1000)

}

object ThreadGuardedBlock extends App {
  val lock = new AnyRef
  var message: Option[String] = None

  val greeter = ThreadProducer.thread {
    lock.synchronized {
      while (message == None)
        lock.wait()
        println(message.get)
    }
  }

  lock.synchronized {
    message = Some("Hello Message")
    lock.notify()
  }

  greeter.join()

}

object ThreadPool extends App {
  val tasks = mutable.Queue[() => Unit]()

  object Worker extends Thread {

    setDaemon(true)

    def pool(): () => Unit = tasks.synchronized {
      while (tasks.isEmpty)
        tasks.wait()
      tasks.dequeue()
    }

  @tailrec override def run() = {
    val task = pool()
    task()
    run()
    }

  }

  Worker.start()

  def asynchronous(body: => Unit) = tasks.synchronized{
    tasks.enqueue( () => body )
    tasks.notify()
  }

  asynchronous{ println("Asynchronous call") }
  Thread.sleep(1000)
  println(Worker.getState)

  asynchronous{ println(s"Asynchronous sum of 1+2 = ${1+2}") }
  Thread.sleep(1000)
  println(Worker.getState)

  asynchronous{ println("Another call") }
  Thread.sleep(1000)
  println(Worker.getState)

  Thread.sleep(1000)
  println(Worker.getState)

}

object ThreadPoolShutdown extends App {

  var alive = true
  val tasks = mutable.Queue[() => Unit]()

  object Worker extends Thread {

    def pool(): Option[() => Unit] = tasks.synchronized {
      while (tasks.isEmpty && alive)
        tasks.wait()
      if(alive)
        Some(tasks.dequeue())
      else
        None
    }

    @tailrec override def run() = pool() match {
      case Some(task) =>
        task()
        run()
      case None => println("Shutting down worker")
    }

    def shutdown() = tasks.synchronized {
      alive = false
      tasks.notify()
    }
  }

  Worker.start()

  def asynchronous(body: => Unit) = tasks.synchronized {
    tasks.enqueue( () => body )
    tasks.notify()
  }

  asynchronous{ println("Asynchronous call") }
  Thread.sleep(1000)
  println(Worker.getState)

  Worker.shutdown()

  asynchronous{ println("Another call") }
  Thread.sleep(1000)
  println(Worker.getState)

  Thread.sleep(1000)
  println(Worker.getState)

}