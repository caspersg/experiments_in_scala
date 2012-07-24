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
import naedyrscala.tools.Async._

class AsyncTest {

  @Test
  def proof() = {
    println("define result")
    val result = async {
      println("executing func in thread " + Thread.currentThread.getId)
      5
    }
    println("available " + result.available)
    Thread.sleep(2000)
    println("result " + result + " in thread " + Thread.currentThread.getId)
    println(result.await)
    println(result.await())
    println(await(result))
    println("available " + result.available)
  }

  @Test
  def threeSums() = {
    val sum = async {
      (1 to 100000000).reduceLeft(_ + _)
    }
    val sum2 = async {
      (1 to 100000000).reduceLeft(_ + _)
    }
    val sum3 = async {
      (1 to 100000000).reduceLeft(_ + _)
    }
    println("do something else")
    println(sum.await)
    println(await { sum })
    println(await(sum3))
  }

  @Test
  def async_await() = {
    val sum = async {
      (1 to 100000000).reduceLeft(_ + _)
    }
    // async await will wait for the result in another thread, ie callback. 
    // same as putting it all in the async block, except broken up in to 2 executions (matters if it's all in the same thread)
    val result = async {
      val x = await(sum)
      println("callback " + x)
      x
    }

    // equivalent to above, except runs together 
    val result2 = async {
      val y = (1 to 100000000).reduceLeft(_ + _)
      println("callback " + y)
      y
    }

    println("do something else")
    println(await(sum))
    println("do another thing else")
    println(result.await)
    println(result2)
  }

  @Test
  def await_async() = {
    // await async will run something synchronously in another thread 
    val sum = await(async {
      (1 to 100000000).reduceLeft(_ + _)
    })

    println("do something else")
    println(sum)
  }

  @Test
  def notDeadlock() = {
    // await async will run something synchronously in another thread 
    val sum1 = async {
      // can't create deadlock, as this block can't be defined in terms of a Result defined below
      (1 to 100000000).reduceLeft(_ + _)
    }

    val sum2 = async {
      await(sum1)
      (1 to 100000000).reduceLeft(_ + _)
    }

    println("do something else")
    println(await(sum1) + await(sum2))
  }

  @Test
  def totalSums() = {
    val total = AtomOptimistic(0)
    val max = 1000
    println("initial total " + total.value)
    val sum = async {
      (1 to max).foldLeft(0) { (x, y) =>
        val value = x + y
        total.value += y
        value
      }
    }
    println("mid total " + total.value)
    val sum2 = async {
      (1 to max).foldLeft(0) { (x, y) =>
        val value = x + y
        total.value += y
        value
      }
    }
    println("mid total " + total.value)
    val sum3 = async {
      (1 to max).foldLeft(0) { (x, y) =>
        val value = x + y
        total.value += y
        value
      }
    }
    println(sum.await)
    println("mid total " + total.get)
    println(sum2.await)
    println(await { sum3 })
    println("total " + total.value + " should be " + (sum.await + sum2.await + sum3.await))
  }

  case class Account(private val initialAmount: Int) {
    val balance = Atom(initialAmount)
    def withdraw(amount: Int) = {
      balance.set { x => println("-" + amount); x - amount }
    }
    def deposit(amount: Int) = {
      balance.set { x => println("+" + amount); x + amount }
    }
  }

  @Test
  def bank() = {
    val account = Account(500)
    val results1 = async {
      account.deposit(100)
      account.withdraw(100)
    }
    val results2 = async {
      account.withdraw(10)
      account.withdraw(10)
    }
    val results3 = async { account.withdraw(10) }
    await(results1)
    await(results2)
    await(results3)
    assertEquals((500 + 100 - 100 - 10 - 10 - 10), account.balance.get)
  }

}