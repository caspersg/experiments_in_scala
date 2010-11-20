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

import java.util.concurrent.Executors
import java.util.concurrent.Callable

object Background {
  val exec = Executors.newCachedThreadPool()
  def apply[T](func: => T): Result[T] = {
    Result(exec.submit(new Callable[T]() {
      def call(): T = {
        //println("func called")
        func
      }
    }))
  }
  case class Result[T](private val future: java.util.concurrent.Future[T]) {
    def apply() = value
    lazy val value = future.get()
  }
}

object FutureTest extends Application {
  proof()
  threeSums()
  totalSums()
  bank()

  def proof() = {
    println("define result")
    val result = Background {
      println("executing func in thread " + Thread.currentThread.getId)
      5
    }
    Thread.sleep(2000)
    println("result " + result + " in thread " + Thread.currentThread.getId)
    println(result())
    println(result.value)
  }

  def threeSums() = {
    val sum = Background {
      (1 to 100000000).reduceLeft(_ + _)
    }
    val sum2 = Background {
      (1 to 100000000).reduceLeft(_ + _)
    }
    val sum3 = Background {
      (1 to 100000000).reduceLeft(_ + _)
    }
    println("do something else")
    println(sum.value)
    println(sum2())
    println(sum3.value)
  }

  def totalSums() = {
    val total = AtomOptimistic(0)
    val max = 1000
    println("initial total " + total.value)
    val sum = Background {
      (1 to max).foldLeft(0) { (x, y) =>
        val value = x + y
        total.value += y
        value
      }
    }
    println("mid total " + total.value)
    val sum2 = Background {
      (1 to max).foldLeft(0) { (x, y) =>
        val value = x + y
        total.value += y
        value
      }
    }
    println("mid total " + total.value)
    val sum3 = Background {
      (1 to max).foldLeft(0) { (x, y) =>
        val value = x + y
        total.value += y
        value
      }
    }
    println(sum.value)
    println("mid total " + total.value)
    println(sum2.value)
    println(sum3())
    println("total " + total.value + " should be " + (sum.value + sum2.value + sum3.value))
  }

  def bank() = {
    val balance = AtomOptimistic(100)
    def withdraw(amount: Int, balance: Int) = {
      if (balance >= amount) {
        balance - amount
      } else {
        throw new RuntimeException("not enough funds")
      }
    }
    def deposit(amount: Int, balance: Int) = {
      balance + amount
    }
    def applyTransaction(account: Atomic[Int], transaction: Int => Int) = {
      Background {
        account(acc => transaction(acc))
        account()
      }
    }

    def transact = applyTransaction(balance, _: Int => Int)
    val transactions = List(deposit(100, _: Int), withdraw(100, _: Int), withdraw(10, _: Int), withdraw(10, _: Int), withdraw(10, _: Int))
    val results = transactions.map { transact(_) }
    println("balance " + balance.value)
    results.foreach(x => println(x.value))
    println("balance " + balance.value + " should equal " + (100 + 100 - 10 - 10 - 10 - 10))
  }
}
