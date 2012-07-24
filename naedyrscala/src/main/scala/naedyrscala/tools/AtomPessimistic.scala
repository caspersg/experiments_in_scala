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

case class AtomPessimistic[T](private var ref: T) extends Atomic[T] with AtomicValue[T] with AtomicAssign[T] with AtomicBean[T] {
  def apply(): T = synchronized { ref }

  def apply(f: T => T): T = {
    Retriable.retriable {
      synchronized {
        val previous = apply()
        ref = f(previous)
        return ref
      }
    }
  }
}

object AtomPessimisticTest extends App {
  val atom = AtomPessimistic(0)
  AtomicTest.run(atom)
  AtomicTest.runValue(atom)
}