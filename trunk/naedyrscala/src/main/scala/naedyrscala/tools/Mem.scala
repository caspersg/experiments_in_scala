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

// atomic memoization
object Mem {
  def mem0[R](func: => R): Function0[R] = {
    lazy val cache = func
    () => {
      cache
    }
  }

  def mem1[A, R](func: A => R): A => R = {
    val cache = AtomOptimistic(new WeakHashMap[A, R]())
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
    val cache = AtomOptimistic(new WeakHashMap[(A, B), R]())
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

