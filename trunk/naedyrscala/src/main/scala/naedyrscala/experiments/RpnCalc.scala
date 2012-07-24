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

// solveRPN :: (Num a, Read a) => String -> a
// solveRPN = head . foldl foldingFunction [] . words
//     where   foldingFunction (x:y:ys) "*" = (x * y):ys
//             foldingFunction (x:y:ys) "+" = (x + y):ys
//             foldingFunction (x:y:ys) "-" = (y - x):ys
//             foldingFunction xs numberString = read numberString:xs

object RpnCalc extends App {

  def solve(input: String) = {
    input.split("\\s+").toList.foldLeft[List[String]](Nil) {
      case (x :: y :: ys, "*") => (x.toFloat * y.toFloat).toString :: ys
      case (x :: y :: ys, "+") => (x.toFloat + y.toFloat).toString :: ys
      case (x :: y :: ys, "-") => (x.toFloat - y.toFloat).toString :: ys
      case (xs, num) => num :: xs
    }.head.toFloat
  }

  assert(solve("2 2 +") == 4)
  assert(solve("2 2 *") == 4)
  assert(solve("2 4 -") == 2)
  assert(solve("1 2 + 5 *") == 15)
}