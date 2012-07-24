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
package naedyrscala.tools

import scala.actors.Actor
import scala.actors.Actor._

import org.junit._
import Assert.assertEquals

class ActorLoadBalanceTest {
  def makeActor(id: Int): Actor = {
    actor {
      loop {
        react {
          case x: String =>
            println("ran " + id + " " + x)
          case Exit =>
            println("exit " + id)
            exit
        }
      }
    }
  }

  @Test
  def testTheTest = {
    val actor = makeActor(100)
    actor ! "hi"
    Thread.sleep(2000)
    actor ! Exit
  }

  @Test
  def testTheRest = {
    val actor = ActorLoadBalance((1 to 4) map {
      makeActor(_)
    })
    (1 to 10) foreach {
      actor ! _.toString
    }
    Thread.sleep(2000)
    actor ! Exit
    Thread.sleep(2000)
  }
}