import akka.actor.Actor.Receive
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

class DoorActor() extends Actor {

  def closed: Actor.Receive = {
    case "open" =>
      println("Opening the door")
      context.become(opened)
    case "enter" =>
      println("You must open the door first!")
  }

  def opened: Receive = {
    case "enter" =>
      println("You are entering")
    case "close" =>
      println("Closing the door")
      context.become(closed)
  }

  override def receive: Receive = closed

}

object DoorActor {

  def props =  Props(classOf[DoorActor])

}

object DoorActorApp extends App {

  val actorSystem = ActorSystem("ActorSystem-02")
  val doorActor: ActorRef = actorSystem.actorOf(DoorActor.props)

  doorActor ! "enter"
  doorActor ! "open"
  doorActor ! "enter"
  doorActor ! "close"

  Thread.sleep(1000)

  actorSystem.terminate()

}

