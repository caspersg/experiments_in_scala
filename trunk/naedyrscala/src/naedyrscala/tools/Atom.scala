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

import java.util.concurrent.atomic.AtomicReference

case class Atom[T](private val initialValue: T) {
  private val ref = new AtomicReference[T]()
  ref.set(initialValue)
  def value = ref.get()
  def value_=(transaction: => T): Unit = {
    while (!ref.compareAndSet(ref.get(), transaction)) {
      // println("retry transaction")
    }
  }
  def value_=(transaction: T => T): Unit = {
    while (!ref.compareAndSet(ref.get(), transaction(ref.get()))) {
      // println("retry transaction")
    }
  }
}

object Main extends Application {
  val myAtom = Atom(5)
  myAtom.value = 5
  println(myAtom.value)
  myAtom.value = 1
  println(myAtom.value)
  myAtom.value = 3
  println(myAtom.value)
}
