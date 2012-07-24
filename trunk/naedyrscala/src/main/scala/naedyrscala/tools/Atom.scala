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

case class Atom[T](private val value: T) {
  private val ref = new AtomicReference[T]()
  ref.set(value)

  def get(): T = ref.get()

  def set(f: T => T): T = {
    val previous = get()
    val update = f(previous)
    if (ref.compareAndSet(previous, update)) {
      update
    } else {
      set(f)
    }
  }

  def set(f: => T): T = set(previous => f)
}
