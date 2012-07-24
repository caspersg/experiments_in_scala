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

class FoldLeft {

  val list = (1 to 10).toSeq

  val result = list.foldLeft(0)(_ + _)
  val min = list.foldLeft(-1)((a, b) => if (a < b) a else b)

  val reversed1 = (List.empty[Int] /: list) { (a, e) => e :: a }
}
