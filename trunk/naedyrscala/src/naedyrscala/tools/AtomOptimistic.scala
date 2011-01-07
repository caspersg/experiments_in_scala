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

/**
 * Atom is an atomic variable. ie a safe way of sharing mutable state
 *
 */
case class AtomOptimistic[T](private val initialValue: T) extends Atomic[T] with AtomicValue[T] with AtomicAssign[T] with AtomicBean[T] {
  private val ref = new AtomicReference[T]()
  ref.set(initialValue)

  def apply(): T = ref.get()

  def apply(f: => T): T = {
    var value = apply()
    retriable {
      do {
        value = f
        println("collision")
        Thread.sleep(1);
      } while (!ref.compareAndSet(ref.get(), value))
    }
    value
  }
}

object AtomOptimisticTest extends Application {
  val atom = AtomOptimistic(0)
  AtomicTest.run(atom)
  AtomicTest.runValue(atom)
  AtomicTest.runBean(atom)

  // assign
  atom(_ + 2)
  atom { _ + 2 }
  atom { old => old + 2 }

  atom := 2
  atom.:=(2)
  atom := (_ + 2)
  atom := { _ + 2 }

  atom.value = 2
  atom.value = (_ + 2)
  atom value = { _ + 2 }
  atom value = 2

  atom set 2
  atom.set(2)
  atom.set { _ + 2 }
  atom set { _ + 2 }

  // dereference
  atom()

  atom.value
  atom value

  atom.get()
  atom.get
  atom get
}