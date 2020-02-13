package com.jackson.akka.actors

import akka.actor.{Actor, ActorRef}

class AActor(bActorRef: ActorRef) extends Actor {
  var ref: ActorRef = bActorRef
  var count = 0

  override def receive: Receive = {
    case "start" => {
      println("Aactor出招了, start ok")
      self ! "我打"
    }
    case "我打" => {
      // 给BActor发送消息
      // 这里需要持有BActor的引用(BActorRef)
      println("AActor(黄飞鸿) 厉害 看我佛山无隐脚")
      Thread.sleep(1000)
      ref ! "我打"
      count += 1
      if (count == 50) {
        context.stop(self)
        context.system.terminate()
      }
    }
  }
}
