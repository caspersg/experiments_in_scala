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
object EdgeCase extends Application {

  def f1(f: Int => Int) {
    println(">> f1")
    println("  got " + f(23))
    println("  got " + f(42))
    println("<< f1")
  }
  
   def f2(f: => Int) {
    println(">> f2")
    println("  got " + f)
    println("  got " + f)
    println("<< f2")
  }

  f1 { println("  >> add 23"); _ + 23 }
  f2 { println("  >> add 23"); 23 }
}

