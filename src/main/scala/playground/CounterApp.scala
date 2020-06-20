package playground

import akka.actor.{Actor, ActorSystem, Props}
import playground.BankApp.BankAccount

object CounterApp extends App {

  object Counter {
    case object Increment
    case object Decrement
    case object Value
  }

  class Counter extends Actor {
    var count:Int = 0
    override def receive: Receive = {
      case Counter.Increment => count += 1
      case Counter.Decrement => count -= 1
      case Counter.Value => println(count)
    }
  }

  val actorSystem =  ActorSystem("akka")
  val counter = actorSystem.actorOf(Props[Counter], "counter")

  counter ! Counter.Increment
  counter ! Counter.Increment
  counter ! Counter.Increment
  counter ! Counter.Value
}
