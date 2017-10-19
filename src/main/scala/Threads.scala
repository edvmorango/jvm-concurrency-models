object ThreadName extends App {

  val current: Thread = Thread.currentThread
  val name = current.getName

  println(name)

}

object ThreadCreation extends App {

  class MyCustomThread(id: Int) extends Thread {

    override def run(): Unit = {
      (0 until 1000).filter(_ % 200 == 0).foreach { _ =>
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