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

object Reflection {
  class AsClass(x: Any) {
    def methods = asClass.getDeclaredMethods.toList
    def fields = asClass.getDeclaredFields.toList
    def asClass = wrapped(x).getClass
    private def wrapped(x: Any): AnyRef = x match {
      case x: Byte => byte2Byte(x)
      case x: Short => short2Short(x)
      case x: Char => char2Character(x)
      case x: Int => int2Integer(x)
      case x: Long => long2Long(x)
      case x: Float => float2Float(x)
      case x: Double => double2Double(x)
      case x: Boolean => boolean2Boolean(x)
      case _ => x.asInstanceOf[AnyRef]
    }
  }

  implicit def any2AsClass(x: Any) = new AsClass(x)
}