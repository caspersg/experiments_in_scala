/*
Copyright 2010 naedyr@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0
       
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package naedyrscala.experiments

import scala.actors.Future
import scala.actors.Actor
import scala.actors.Actor._

object Test2 extends Application {

  case class Exit()

  println("async")
  val echoActorAsync = actor {
    loop {
      react {
        case x: String =>
          Thread.sleep(1000)
          println("echo " + x)
        case Exit => exit
      }
    }
  }
  echoActorAsync ! "hello"
  println("waiting")
  Thread sleep 2000
  echoActorAsync ! Exit
  println("done\n")

  println("sync")
  val echoActorSync = actor {
    loop {
      react {
        case x: String =>
          Thread.sleep(1000)
          reply(x)
        case Exit => exit
      }
    }
  }
  echoActorSync !? "hello" match {
    case result: String => println("result " + result)
  }
  echoActorSync ! Exit
  println("done\n")

  println("future")
  val echoActorFuture = actor {
    loop {
      react {
        case x: String =>
          Thread.sleep(1000)
          reply("echo " + x)
        case Exit => exit
      }
    }
  }
  echoActorFuture !! "hello" match {
    case x: Future[String] =>
      if (!x.isSet) {
        println("future not set")
      }
      val value = x()
      println(value)
    case x => println("error, recieved " + x)
  }
  echoActorFuture ! Exit
  println("done\n")
}

