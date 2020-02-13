package com.jackson.akka.actors

import akka.actor.{ActorSystem, Props}

object ActorGame {
  val as = ActorSystem("as")

  def main(args: Array[String]): Unit = {
    // 先创建  B
    val bRef = as.actorOf(Props[BActor], "B")
    val aRef = as.actorOf(Props(new AActor(bRef)), "A")
    aRef ! "start"

  }
}
