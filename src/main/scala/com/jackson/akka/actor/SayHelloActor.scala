package com.jackson.akka.actor

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

// 1. 当我们继承Actor后，就是一个Actor，核心方法 receive方法重写
class SayHelloActor extends Actor {
  // receive 方法 会被该Actor的mailBox(实现Runable接口的)调用
  // 当Actor的mailbox接收到消息就会调用receive
  // scala.PartialFunction[scala.Any, scala.Unit]
  override def receive: Receive = {
    case "hello" => println("收到hello,回应hello too")
    case "ok" => println("收到hello, 回应ok too:")
    case "exit" => {
      context.stop(self)
      context.system.terminate()
    }
    case _ => println("匹配不到")
  }
}

object SayHelloActorDemo {
  // 先创建一个ActorSystem，专门用于创建Actor
  private val actoryFactory: ActorSystem = ActorSystem("actoryFactory")
  // 2. 创建一个actor的同时， 返回actor的Ref, Props[SayHelloActor] 使用了反射
  // "sayHelloActor" 名字
  // sayHelloActorRef Ref
  private val sayHelloActorRef: ActorRef = actoryFactory.actorOf(Props[SayHelloActor], "sayHelloActor")


  def main(args: Array[String]): Unit = {
    // 给sayhelloActor发消息 (到邮箱)
    sayHelloActorRef ! "hello"
    sayHelloActorRef ! "exit"
    // 如何退出


  }
}
