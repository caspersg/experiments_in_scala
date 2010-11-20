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

case class ThreadResult[T](private val future: java.util.concurrent.Future[T]) extends Result[T] {
  private lazy val value = future.get()
  def await(): T = value
  def available(): Boolean = future.isDone()
}

case class AsyncThread[T]() extends Async[T] {
  def invoke[T](func: => T): Result[T] = {
    ThreadResult(AsyncThread.threadPool.submit(new Callable[T]() {
      def call(): T = {
        func
      }
    }))
  }
}

object AsyncThread {
  val threadPool = Executors.newCachedThreadPool()
  def async[T](func: => T): Result[T] = {
    AsyncThread().invoke(func)
  }
  def await[T](result: Result[T]): T = result.await()
}