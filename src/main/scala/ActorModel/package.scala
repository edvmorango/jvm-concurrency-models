import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class SomeActor(password: String) extends Actor {

  def receive = {
    case "hello" =>
      println("hello -> Hi, how are you?")
    case "password" =>
      println(s"password -> The password is: $password")
    case "bye" =>
      println("bye -> Bye, see you later.")
    case _ =>
      println("_ -> Wut?")
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
      println("open -> Opening the door")
        context.become(opened)
    case "enter" =>
      println("enter -> You must open the door first!")
  }

  def opened: Receive = {
    case "enter" =>
      println("enter -> You are entering")
    case "close" =>
      println("close -> Closing the door")
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

class EmployerActor extends Actor {

  def receive = {

    case "boss" =>
      val parent = context.parent
      println(s"boss -> I am ${context.self}, my boss is $parent")

  }

  override def postStop() = {
    println(s"I ${context.self} was fired")
  }

}

class BossActor extends Actor {

  def receive = {
    case "hire" =>
      println("Hire -> Hiring a new employer...")
      context.actorOf(Props[EmployerActor])
    case "askwhoistheboss" =>
      println("askwhoistheboss -> Asking who is the boss")
      context.children.foreach(_ ! "boss")
    case "fire" =>
      context.stop(context.children.head)
    case "fireAll" =>
      println("fireAll -> Good bye everyone")
      context.stop(self)
  }

}

object BossActorApp extends App {

  val actorSystem = ActorSystem("ActorSystem-03")
  val bossActor: ActorRef = actorSystem.actorOf(Props[BossActor])

  bossActor ! "hire"

  Thread.sleep(1000)

  bossActor ! "hire"

  Thread.sleep(1000)

  bossActor ! "hire"

  Thread.sleep(1000)

  bossActor ! "askwhoistheboss"

  Thread.sleep(1000)

  bossActor ! "fire"

  Thread.sleep(1000)

  bossActor ! "askwhoistheboss"

  Thread.sleep(1000)

  bossActor ! "fireAll"

  Thread.sleep(1000)

  actorSystem.terminate()

}


