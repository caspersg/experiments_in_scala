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

// atomic memoization
object Mem {
  def mem0[R](func: => R): Function0[R] = {
    lazy val cache = func
    () => {
      cache
    }
  }

  def mem1[A, R](func: A => R): A => R = {
    val cache = AtomOptimistic(Map[A, R]())
    (a: A) => {
      if (cache.value.contains(a)) {
        cache.value(a)
      } else {
        val result = func(a)
        cache.value += (a -> result)
        result
      }
    }
  }

  def mem2[A, B, R](func: (A, B) => R): (A, B) => R = {
    val cache = AtomOptimistic(Map[(A, B), R]())
    (a: A, b: B) => {
      if (cache.value.contains((a, b))) {
        cache.value((a, b))
      } else {
        val result = func(a, b)
        cache.value += ((a, b) -> result)
        result
      }
    }
  }

  def apply[R](func: => R) = mem0(func)
  def apply[A, R](func: A => R) = mem1(func)
  def apply[A, B, R](func: (A, B) => R) = mem2(func)
}

object MemTest extends Application {
  def one(): Int = { println("one"); 1 }
  def mult(x: Int, y: Int): Int = { println("mult"); x * y }
  def double(x: Int): Int = { println("double"); mult(x, 2) }
  def hello(x: String): String = { println("hello"); "hello " + x }

  val memOne = Mem(one)
  memOne()
  memOne()
  memOne()

  val memDouble = Mem(double _)
  memDouble(2)
  memDouble(2)
  memDouble(4)
  memDouble(4)
  memDouble(4)

  val memMult = Mem(mult _)
  memMult(2, 3)
  memMult(2, 3)
  memMult(4, 4)
  memMult(4, 4)

  val memHello = Mem(hello _)
  memHello("me")
  memHello("me")
}