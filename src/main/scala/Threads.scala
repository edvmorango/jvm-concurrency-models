object ThreadName extends App {

  val current: Thread = Thread.currentThread
  val name = current.getName

  println(name)

}

object ThreadCreation extends App {

  class MyCustomThread(id: Int) extends Thread {

    override def run(): Unit = {
      (0 until 10000).filter(_ % 5000 == 0).foreach { _ =>
        println(s"MyCustomThread instance is running, his name is: $getName, his identifier is:  $id")
      }
    }

  }

  val someThread = new MyCustomThread(0)
  val anotherThread = new MyCustomThread(1)
  val oneMoreThread = new MyCustomThread(2)

  someThread.start()
  anotherThread.start()
  oneMoreThread.start()

}

object ThreadJoin extends App {

  class MyCustomThread(name: String = "MyCustomThread", times: Int = 10) extends Thread {

    override def run(): Unit = {
      (0 until times).foreach { _ =>
        Thread.sleep(1000)
        println(s"Executing $name")
      }
    }

  }

  val someThread = new MyCustomThread
  val anotherThread = new MyCustomThread("AnotherCustomThread", 20)

  someThread.start()
  anotherThread.start()

  println(s"\nMyCustomThread will join")
  someThread.join()
  println("\nMyCustomThread joined\n")

  println("Finishing MainThread")

}