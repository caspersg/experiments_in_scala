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

class MyClass {
  val rnd = scala.util.Random

  def method1(): Double = {
    rnd.nextDouble
  }

  def method2(): Double = {
    rnd.nextDouble
  }

  def method3(): Double = {
    rnd.nextDouble
  }

  def method4(): Double = {
    rnd.nextDouble
  }

  def method5(): Double = {
    rnd.nextDouble
  }

  var m = Map[String, Double]()
  m += { "key1" -> method1 }
  m += { "key2" -> method2 }
  m += { "key3" -> method3 }
  m += { "key4" -> method4 }
  m += { "key5" -> method5 }
  
  val w = Map( "key1" -> {() => rnd.nextDouble},
  "key2" -> {() => rnd.nextDouble},
  "key3" -> {() => rnd.nextDouble},
  "key4" -> {() => rnd.nextDouble},
  "key5" -> {() => rnd.nextDouble})
  
  def computeValues(keyList: List[String]): Map[String, Double] = {
    var map = Map[String, Double]()
    keyList.foreach(factor => {
      val value = w(factor)
      map += { factor -> value() }
    })
    map
  }

}

object MapFuncTest {
  def main(args: Array[String]) {
    val b = new MyClass
    for (i <- 0 until 3) {
      val computedValues = b.computeValues(List("key1", "key4"))
      computedValues.foreach(element => println(element._2))
    }
  }
}