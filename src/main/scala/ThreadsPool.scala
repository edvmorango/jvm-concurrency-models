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

    def pool() = tasks.synchronized {
      while (tasks.isEmpty)
        tasks.wait()
      tasks.dequeue()
    }

    override def run() = {
      while (true) {
        val task = pool()
        task()
      }
    }
  }

  Worker.start()

  def asynchronous(body: => Unit) = tasks.synchronized{
    tasks.enqueue( () => body )
    tasks.notify()
  }

  asynchronous{ println("1") }
  Thread.sleep(1000)
  println(Worker.getState)

  asynchronous{ println("2") }
  Thread.sleep(1000)
  println(Worker.getState)

  asynchronous{ println("3") }
  Thread.sleep(1000)
  println(Worker.getState)

  Thread.sleep(1000)
  println(Worker.getState)

}