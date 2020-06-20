package playground

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import playground.ChanginActorBehavior.Mom.{Food, MomStart}


object ChanginActorBehavior extends App {

  object FussyKid {

    case object KidAccept
    case object KidReject
    val HAPPY = "happy"
    val SAD = "sad"
  }

  class FussyKid extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive()

    def happyReceive(): Receive = {
      case Food(VEGETABLE) => context.become(sadReceive())
      case Food(CHOCOLATE) =>
      case Ask(_) => sender() ! KidAccept
    }

    def sadReceive(): Receive = {
      case Food(CHOCOLATE) => context.become(happyReceive())
      case Food(VEGETABLE) =>
      case Ask(_) => sender() ! KidReject
    }

  }

  object Mom {

    case class MomStart(kidRef: ActorRef)
    case class Food(name: String)
    case class Ask(message: String)
    val VEGETABLE = "vegetable"
    val CHOCOLATE = "chocolate"
  }

  class Mom extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = {
      case MomStart(kidRef) =>
        kidRef ! Food(VEGETABLE)
        kidRef ! Ask("")
      case KidReject => println("my children is fussy")
      case KidAccept => println("my children is happy")
    }
  }

  val actorSystem = ActorSystem("akka")
  val mom = actorSystem.actorOf(Props[Mom], "mom")
  val kid = actorSystem.actorOf(Props[FussyKid], "kid")
  mom ! MomStart(kid)
}
