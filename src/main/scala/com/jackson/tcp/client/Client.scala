package com.jackson.tcp.client

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.jackson.tcp.common.{Message, Recall}
import com.jackson.tcp.server.YoudiSever
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

class Client(severHost: String, serverPort: Int, serverName: String) extends Actor {
  // 定义server Ref
  var serverActorRef: ActorSelection = _

  // 在actor中有一个Prestart方法，在actor执行前执行
  // 在akka开发中， 通常将初始化的工作，放在preStart方法中


  override def preStart(): Unit = {
    println("prestart")
    serverActorRef = context.actorSelection(s"akka.tcp://${serverName}@${severHost}:${serverPort}/user/youdiServer")
    println("ref", serverActorRef)
  }

  override def receive: Receive = {
    case "start" => println("client 开始工作")
    case ms: String => {
      // 发给服务器
      serverActorRef ! Message(ms) // 使用clientMessage case class apply
    }
    case Recall(msg) => { // 提取器
      println("客服回复:" + msg)
    }
  }
}

object Client {
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = 9998
    val serverHost = "127.0.0.1"
    val serverPort = 9999
    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$host
         |akka.remote.netty.tcp.port=$port
         """.stripMargin)
    //  ActorSystem
    val clientAs = ActorSystem("Client", config)
    val ref = clientAs.actorOf(Props(new Client(serverHost, serverPort, "Server")), "clientName")

    ref ! "start"

    while (true) {
      println("请输入你要咨询的问题")
      val ms = StdIn.readLine()
      ref ! ms
    }


  }
}
