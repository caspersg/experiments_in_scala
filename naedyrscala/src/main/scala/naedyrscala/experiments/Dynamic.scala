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

case class Runner() {
  def run(times: Int): Int = {
    (1 to times).foreach(x => println("run " + x))
    times
  }
}

case class Walker() {
  def walk(times: Int): String = (1 to times).map("run" + _).mkString
}

object RunTest extends Application {
  type Run = { def run(times: Int): Int }
  type Walk = { def walk(times: Int): String }

  def doRun(runner: Run) = runner.run(2)
  def doWalk(walker: Walk) = walker.walk(2)

  val times: Int = Runner().asInstanceOf[Run].run(5)
  val outW: String = Walker().asInstanceOf[Walk].walk(3)
  doRun(Runner())
  doWalk(Walker())

  val out: String = Runner().asInstanceOf[Walk].walk(3)
  val timesW: Int = Walker().asInstanceOf[Run].run(5)

  val result: Int = Runner().asInstanceOf[{ def run(x: Int): Int }].run(1)

  def run(o: { def run(x: Int): Int }, times: Int) = o.run(times)
  val result2 = run(Runner(), 4)

  val result3: Int = (5: Any).asInstanceOf[{ def run(x: Int): Int }].run(1)

  val result4: Int = 5.asInstanceOf[{ def run(x: Int): Int }].run(1)

  val result5 = Runner() match {
    case x: { def run(x: Int): Int } => x.run(2)
    case _ => -1
  }

  // without the :Any it fails compilation
  val result6 = (100: Any) match {
    case x: { def run(x: Int): Int } => x.run(2)
    case _ => -1
  }
}