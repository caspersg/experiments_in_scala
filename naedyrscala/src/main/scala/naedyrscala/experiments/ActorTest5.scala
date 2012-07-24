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

object Test5 extends App {
  case class Exit()
  case class Msg(msg: String)
  class MyActor(val code: Int) extends Actor {
    def act() = loop {
      react {
        case Msg(x) =>
          // wait 1 second
          Thread.sleep(1000)
          println(code + " " + x)
        case Exit =>
          println(code + " exit")
          exit
      }
    }
  }
  def run(range: Range): Unit = {
    range.foreach { x =>
      val actor = new MyActor(x)
      actor.start()
      actor ! Msg("msg " + x)
      actor ! Exit
    }
  }

  run(1 to 10)

  //    readLine
  //    run(1 to 100)
  //    
  //    readLine
  //    // thousand
  //    run(1 to 1000)
  //    
  //    readLine
  //    run(1 to 10000)
  //    
  //    readLine
  //    run(1 to 100000)
  //    
  readLine
  // a million
  run(1 to 1000000)
  //    
  //    readLine
  //    run(1 to 10000000)
  //    
  //    readLine
  //    run(1 to 100000000)
  //    
  //    readLine
  //    run(1 to 100000000)

  // readLine
  // a billion
  // run(1 to 1000000000)

  println("done\n")
}