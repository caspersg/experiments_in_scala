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

class FizzBuzz {
  // Write a program that prints the numbers from 1 to 100. 
  // But for multiples of three print Fizz instead of the number and for the multiples of five print Buzz. 
  // For numbers which are multiples of both three and five print FizzBuzz. 
  def getVal(x: Int) = {
    if (x % 3 == 0 && x % 5 == 0) "FizzBuzz"
    else if (x % 3 == 0) "Fizz"
    else if (x % 5 == 0) "Buzz"
    else x.toString()
  }

  (1 to 100) foreach { x =>
    println(getVal(x))
  }
}