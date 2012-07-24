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

import org.junit._
import Assert.assertEquals

class InterpolationTest extends InterpolationContext {
  val a = "3"
  val c = "a c"
  val d = 21

  @Test
  def test() {
    assertEquals("expanded: 3; not expanded: ${b}; more text", {
      "expanded: ${a}; not expanded: ${b}; more text"/
    })
    assertEquals("expanded: 3; expanded: a c; more text", {
      "expanded: ${a}; expanded: ${c}; more text".i
    })
    assertEquals("expanded: 3; expanded: 21; more text", {
      "expanded: ${a}; expanded: ${d}; more text" i
    })
  }
}