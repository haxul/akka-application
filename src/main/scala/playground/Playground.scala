package playground

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object Playground extends App {
  val actorSystem = ActorSystem("akkaActor")

  class WordCounter extends Actor {
    var totalWords = 0
    override def receive: Receive = {
      case message: String =>
        println(s"I got a message: $message")
        totalWords += message.split(" ").length
      case msg => println(s"Unknown message $msg")
    }
  }

  val wordCounter: ActorRef = actorSystem.actorOf(Props[WordCounter], "WordCounter")

  object Person {
    def props(name:String):Props = Props(new Person(name))
  }
  
  class Person(name:String) extends Actor {
    override def receive: Receive = {
      case "name" => println(s"my name is $name")
      case message:String => println(s"i got $message")
      case list: List[_] => println(list)
      case _  => println("list of string")
    }
  }

  val person = actorSystem.actorOf(Person.props("sergei"))

  person ! "123213"
  person ! "name"
  person ! List(1,23)
  person ! List("hell ow")
}

