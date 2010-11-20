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

import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.Future

object Test4 extends Application {

  case class Exit()

  println("receive")
  val echoActorReceive = actor {
    while (true) {
      receive {
        case x: String =>
          Thread.sleep(1000)
          println("Receive echo " + x)
        case Exit => exit
      }
    }
  }

  for (i <- 1 to 1000)
    echoActorReceive ! ("hello " + i)

  echoActorReceive ! Exit
  println("done\n")
  // 4 threads total

  println("react")
  val echoActorReact = actor {
    loop {
      react {
        case x: String =>
          Thread.sleep(1000)
          println("React echo " + x)
        case Exit => exit
      }
    }
  }

  for (i <- 1 to 1000)
    echoActorReact ! ("hello " + i)

  echoActorReact ! Exit

  println("done\n")
  // 7 threads total

  case class Msg(msg: String)
  class MyActor(val code: Int) extends Actor {
    def act() = loop {
      react {
        case Msg(x) =>
          println(code + " " + x)
        case Exit =>
          println(code + " exit")
          exit
      }
    }
  }

  val actors = List(new MyActor(0), new MyActor(1), new MyActor(2))
  actors.foreach(_.start())
  (1 to 50).foreach { x =>
    actors(x % actors.length) ! Msg("msg " + x)
  }
  actors.foreach(_ ! Exit)

  println("done\n")
}

