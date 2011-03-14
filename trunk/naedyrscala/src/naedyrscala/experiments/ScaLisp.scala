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
package naedyrscala.experiments

import org.junit.Test
import naedyrscala.tools.AtomOptimistic

trait ScaLisp {
    
    
  sealed case class Expression()
  sealed case class Number(value:Int) extends Expression
  sealed case class String(value:String) extends Expression
  sealed case class Function(block:Expression)

  type Name = Symbol

  /**
   * map of symbol to value
   */
  val scope = AtomOptimistic(Map[Name, Func]())

  case class Value(value: AnyVal)

  abstract class Func()
  sealed case class Func0[A](x: Function0[A]) extends Func
  sealed case class Func1[A, B](x: Function1[A, B]) extends Func
  // .. etc
  object Func {
    def apply[A](x: Function0[A]): Func0[A] = Func0(x)
    def apply[A, B](x: Function1[A, B]): Func1[A, B] = Func1(x)
  }

  def define(name: Name, value: Value) {
    define(name, Func(() => value))
  }

  def define(name: Name, func: Func) {
    scope.set { current =>
      current + (name -> func)
    }
  }

  @Test
  def test() {
    define('one, Value(1))
    define('plusOne, Func((x: Int) => x + 1))
  }
}