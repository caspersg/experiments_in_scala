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

import org.junit._
import Assert.assertEquals

class MemTest {

  def factorial(n: Int) = {
    def loop(n: Int, acc: Int): Int = {
      println("loop " + n + " " + acc)
      if (n <= 0) acc else loop(n - 1, acc * n)
    }
    loop(n, 1)
  }

  @Test
  def test() {
    assertEquals(1, {
      factorial(1)
    })

    assertEquals(120, {
      factorial(5)
    })

    val mfact = Mem(factorial(_))

    assertEquals(1, {
      mfact(1)
    })

    assertEquals(120, {
      mfact(5)
    })
  }

  @Test
  def memTest() {
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

}