package playground

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object CapActor extends App {

  class SimpleActor extends Actor {

    override def receive: Receive = {
      case "Hi!" =>
        println(sender() + "-----------" + self)
//        context.sender() ! "hello, there"
      case message: String =>
        println(s"sender= $sender(), self=$self $message")
        sender() ! "Hi!"
      case SpecialMessage(name, title) => println(name, title)
      case SendFriend(actor) => actor ! s"Hi!"
      case Message(text, bob) => bob ! text
    }
  }
  case class SpecialMessage(name:String, title:String)
  case class SendFriend(name:ActorRef)
  case class Message(text:String, actorRef: ActorRef)

  val actorSystem = ActorSystem("akka")
  val alice = actorSystem.actorOf(Props[SimpleActor], "alice")
  val bob = actorSystem.actorOf(Props[SimpleActor], "bob")
  alice ! Message("hello", bob)
}
