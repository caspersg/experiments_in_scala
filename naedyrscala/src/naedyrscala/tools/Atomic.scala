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

trait Atomic[T] {
  /**
   * @return the current value
   */
  def apply(): T
  /**
   * set a new value, override current value
   * @param f function which calculates the new value
   * @return the value that was set (not the pre or post value, the one set from this invocation)
   */
  def apply(f: => T): T
  /**
   * set a new value, that's derived from the current value
   * @param f function which calculates the new value
   * @return the value that was set (not the pre or post value, the one set from this invocation)
   */
  def apply(f: T => T): T = apply(f(apply()))

  /**
   * retry the new value function (ie retry the transaction)
   */
  def retry(): Unit = throw new RetryException

  /** retry the function if a RetryException is called
   * @param f
   */
  protected def retriable(f: => Unit): Unit = {
    try {
      f
    } catch {
      case _: RetryException => {
        println("retry")
        retriable(f)
      }
    }
  }
  class RetryException extends Exception
}

/**
 * Atomic interface which uses the value member, same as apply methods
 *
 */
trait AtomicValue[T] { self: Atomic[T] =>
  def value: T = apply()
  def value_=(f: => T): Unit = apply(f)
  def value_=(f: T => T): Unit = apply(f)
}

trait AtomicAssign[T] extends Atomic[T] {
  def :=(f: => T): T = apply(f)
  def :=(f: T => T): T = apply(f)
}

trait AtomicBean[T] { self: Atomic[T] =>
  def get(): T = apply()
  def set(f: => T): T = apply(f)
  def set(f: T => T): T = apply(f)
}

object AtomicTest {
  def run(myAtom: Atomic[Int]) {
    myAtom { 2 }
    assert(2 == myAtom())

    myAtom(5)
    assert(5 == myAtom())

    // increment the current value
    myAtom { _ + 1 }
    assert(6 == myAtom())

    // make a second copy of the same shared data
    val myAtomCopy = myAtom

    // double the current value
    myAtomCopy(_ * 2)
    assert(12 == myAtom())
  }

  def runAssign(myAtom: AtomicAssign[Int]) {
    val item = myAtom := (_ * 2)
    assert(24 == myAtom())

    var start = System.currentTimeMillis()
    myAtom := {
      println("do")
      if (System.currentTimeMillis() < start + 50) {
        myAtom.retry()
      }
      println("some")
      println("stuff")
      4
    }
    assert(4 == myAtom())
  }

  def runValue(myAtom: AtomicValue[Int]) {
    myAtom.value = { 2 }
    assert(2 == myAtom.value)

    myAtom.value = 10
    assert(10 == myAtom.value)

    myAtom.value = { _ / 2 }
    assert(5 == myAtom.value)

    // make a second copy of the same shared data
    val myAtomCopy = myAtom

    // decrement
    myAtomCopy.value = _ - 1
    assert { 4 == myAtom.value }
  }

  def runBean(myAtom: AtomicBean[Int]) {
    myAtom.set { 2 }
    assert(2 == myAtom.get)

    myAtom.set(10)
    assert(10 == myAtom.get)

    myAtom.set { _ / 2 }
    assert(5 == myAtom.get())

    // make a second copy of the same shared data
    val myAtomCopy = myAtom

    // decrement
    myAtomCopy set (_ - 1)
    assert { 4 == (myAtom get) }
  }
}