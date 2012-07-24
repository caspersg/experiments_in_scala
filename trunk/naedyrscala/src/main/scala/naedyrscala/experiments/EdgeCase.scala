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

// from http://downgra.de/2010/08/05/scala_gotcha_blocks_and_functions/
object EdgeCase extends App {

  var x = 1

  def f1(f: Int => Int) {
    println(">> f1")
    println("  got " + f(1))
    x += 1
    println("  got " + f(2))
    println("<< f1")
  }

  // ignores the println in the function definition,
  // looks for a function within the block, not just treating the block as the function
  // _very_ edge case. how often will you have statement then _
  f1 {
    println("  >> add 4")
    _ + 4
  }

  // works
  f1 { _ + x }

  // the proper way to do f1
  f1 { x => println("  >> add 4"); x + 4 }

  def f2(f: => Int) {
    println(">> f2")
    println("  got " + f)
    println("  got " + f)
    println("<< f2")
  }

  f2 { println("  >> add 4"); 4 }

}

