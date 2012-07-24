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

object Act {
  case class Exit()
  def apply[T](f: T => Unit) =
    actor {
      loop {
        react {
          case x: T => f(x)
          case Exit => exit
        }
      }
    }

  /*def apply[T](f: T) = {
    actor {
      loop {
        react {
          f
        }
      }
    }
  }*/
}

object Test2 extends App {

  val echo1 = actor {
    loop {
      react {
        case x: String =>
          Thread.sleep(1000)
          println("echo " + x)
        case Act.Exit => exit
      }
    }
  }
  echo1 ! "hello"
  Thread sleep 2000
  echo1 ! Act.Exit
  println("done\n")

  val echo2 = Act { x: String =>
    Thread.sleep(1000)
    println("echo " + x)
  }

  echo2 ! "hello"
  Thread sleep 2000
  echo2 ! Act.Exit
  println("done\n")
}