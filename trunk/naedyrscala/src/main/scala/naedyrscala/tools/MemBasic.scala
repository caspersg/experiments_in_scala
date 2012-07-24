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
import scala.collection.mutable.WeakHashMap

object MemBasic {
  def mem1[A, R](func: A => R): A => R = {
    var cache = new WeakHashMap[A, R]()
    (a: A) => {
      if (cache.contains(a)) {
        cache(a)
      } else {
        val result = func(a)
        cache += a -> result
        result
      }
    }
  }

  def mem2[A, B, R](func: (A, B) => R): (A, B) => R = {
    var cache = new WeakHashMap[(A, B), R]()
    (a: A, b: B) => {
      if (cache.contains((a, b))) {
        cache((a, b))
      } else {
        val result = func(a, b)
        cache += (a, b) -> result
        result
      }
    }
  }

  def apply[A, R](func: A => R) = mem1(func)
  def apply[A, B, R](func: (A, B) => R) = mem2(func)
}

object MemBasicTest extends Application {
  val mult = (x: Int, y: Int) => { println("mult"); x * y }
  val double = (x: Int) => { println("double"); mult(x, 2) }
  val hello = (x: String) => { println("hello"); "hello " + x }

  val memDouble = MemBasic(double)
  memDouble(2)
  memDouble(2)
  memDouble(4)
  memDouble(4)
  memDouble(4)

  val memMult = MemBasic(mult)
  memMult(2, 3)
  memMult(2, 3)
  memMult(4, 4)
  memMult(4, 4)

  val memHello = MemBasic(hello)
  memHello("me")
  memHello("me")

  case class MyObj(val value: Int) {
    def multiply(x: Int) = x * value
  }

  val memMyTriple = MemBasic(MyObj(3).multiply(_: Int))
  memMyTriple(2)
  memMyTriple(2)
}