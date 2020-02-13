package com.jackson.akka.actors

import akka.actor.Actor

class BActor extends Actor {
  var count = 0

  override def receive: Receive = {
    case "我打" => {
      println("Bactor(乔峰) 挺猛 看我降龙十八掌")
      Thread.sleep(1000)
      // 通过sender() 可以获取到发送消息的actor ref
      sender() ! "我打"
      count += 1
      if (count == 50) {
        context.stop(self)
        context.system.terminate()
      }
    }
  }
}
