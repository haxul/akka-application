package playground

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object BankApp extends App {

  case class SuccessOperation(amount: Int, name: String, activity: String)

  case class FailureOperation(amount: Int, name: String, activity: String)

  case class Deposit(name: String, amount: Int)

  case class Withdraw(name: String, amount: Int)

  case class Balance(name: String)

  class BankAccount extends Actor {
    val responder: ActorRef = actorSystem.actorOf(Props[Responder], "responder")
    val storage: mutable.Map[String, Int] = scala.collection.mutable.Map().withDefaultValue(0)

    override def receive: Receive = {

      case Deposit(name, amount) =>
        val curAmount: Int = storage(name)
        storage += (name -> (curAmount + amount))
        responder ! SuccessOperation(amount, name, "deposit")
      case Withdraw(name, amount) =>
        val curAmount: Int = storage(name)
        if (curAmount == 0 || (curAmount - amount) < 0) responder ! FailureOperation(amount, name, "withdraw")
        else {
          storage += (name -> (curAmount - amount))
          responder ! SuccessOperation(amount, name, "withdraw")
        }
      case Balance(name) => println(s"balance of $name is ${storage(name)}")
    }
  }

  class Responder extends Actor {
    override def receive: Receive = {
      case SuccessOperation(amount, name, activity) => println(s"$name ${activity}s $amount")
      case FailureOperation(amount, name, activity) => println(s"$name failed to ${activity} $amount")
    }
  }

  val actorSystem = ActorSystem("akka")
  val accounter = actorSystem.actorOf(Props[BankAccount], "accounter")

  accounter ! Deposit("sergei", 100)
  accounter ! Withdraw("sergei", 50)
  accounter ! Balance("sergei")
}
