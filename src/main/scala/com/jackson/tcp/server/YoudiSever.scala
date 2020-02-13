package com.jackson.tcp.server

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.jackson.tcp.common.{Message, Recall}
import com.typesafe.config.ConfigFactory

class YoudiSever extends Actor {
  override def receive: Receive = {
    case "start" => println("客服开始工作了")
    case Message(mes) => {
      mes match {
        case "姓名" => sender() ! Recall("孙艺珍")
        case "年龄" => sender() ! Recall("永远18")
        case "地址" => sender() ! Recall("韩国")
        case "电影" => sender() ! Recall("假如爱有天意")
      }
    }
  }
}


// 主程序

object YoudiSever {
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = 9999
    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$host
         |akka.remote.netty.tcp.port=$port
         """.stripMargin)
    //  ActorSystem
    val serverAs = ActorSystem("Server", config)
    val ref = serverAs.actorOf(Props[YoudiSever], "youdiServer")

    // 启动
    ref ! "start"

  }
}