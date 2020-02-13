package com.jackson.akka.actor

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
 * 当程序执行 private val sayHelloActorRef: ActorRef = actoryFactory.actorOf(Props[SayHelloActor], "sayHelloActor")
 * 会完成如下任务
 * 1. actoryFactory是ActorSystem("actoryFactory") 创建的
 * 2. 这里的 Props[SayHelloActor]会使用反射机制，创建一个Actor对象，如果是actoryFactory.actorOf(Props(new Actor(SayHelloActor)), name)
 * 形式，就是使用new方式创建一个Actor对象， 注意Props是小括号
 * 3. 会创建一个AActor对象的代理对象aActorRef,使用aActorRef才能发送消息
 * 4. 会在底层创建Dispather Message，是一个线程池， 用于分发消息，消息是发送到对应的Actor的MailBox
 * 5.会在底层创建AActor的mailBox对象， 该对象是一个队列，可接收Dispatcher Message发送的消息
 * 6. MailBox实现了Runable接口，是一个线程，一直运行并调用Actor的receive方法，因此当dispatcher发送消息到MailBox时，actor在receive方法可以得到该消息
 * 7. aActorRef ! "Hello" 表示将hello消息发送到 AActor的mailBOx，通过Disptcher Message转发。
 */


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
