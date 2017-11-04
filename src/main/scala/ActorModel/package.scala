import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class SomeActor(password: String) extends Actor {

  val info = context.system

  def receive = {
    case "hello" =>
      println("Hi, how are you?")
    case "password" =>
      println(s"The password is: $password")
    case "bye" =>
      println("Bye, see you later.")
    case _ =>
      println("Wut?")
  }
}

object SomeActor {

  def props(password: String) = { Props(classOf[SomeActor], password) }
}

object SomeActorApp extends App {

  val actorSystem = ActorSystem("ActorSystem-01")

  val someActor: ActorRef = actorSystem.actorOf(SomeActor.props("123"))

  println(actorSystem)

  someActor ! "hello"

  Thread.sleep(1000)

  someActor ! "password"

  Thread.sleep(1000)

  someActor ! "bty"

  Thread.sleep(1000)

  someActor ! "bye"

  actorSystem.terminate()

  Thread.sleep(1000)

  someActor ! "bye"
}

