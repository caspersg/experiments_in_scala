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

object Test3 extends Application {
  case class Accumulate(amount: Int)
  case class Reset()
  case class Total()

  object Accumulator extends Actor {
    def act = {
      var sum = 0
      loop {
        react {
          case Accumulate(n) =>
            sum += n
          case Reset =>
            sum = 0
          case Total =>
            reply(sum)
            exit
        }
      }
    }
  }

  Accumulator.start
  for (i <- (1 to 100)) {
    Accumulator ! Accumulate(i)
  }

  Accumulator !? Total match {
    case result: Int => println("Accumulator result " + result)
  }
  println("done")
}

