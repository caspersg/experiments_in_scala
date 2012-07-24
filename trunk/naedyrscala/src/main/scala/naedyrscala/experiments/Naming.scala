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

import scala.collection.mutable.ArrayBuffer

// from http://www.objectmentor.com/resources/articles/Naming.pdf
object Naming extends Application {

  // list1 = []
  // for x in theList:
  //   if x[0] == 4:
  //   list1 += x;
  // return list1

  def first(theList: Array[Array[Int]]) = {
    var list1 = ArrayBuffer[Array[Int]]()
    theList.foreach { x =>
      if (x(0) == 4) {
        list1 += x
      }
    }
    list1
  }

  trait Cell {
    def isFlagged(): Boolean
  }

  // flaggedCells = []
  // for cell in theBoard:
  //   if cell.isFlagged():
  //   flaggedCells += cell
  // return flaggedCells
  def second(theBoard: Array[Cell]) = {
    var flaggedCells = ArrayBuffer[Cell]()
    theBoard.foreach { cell =>
      if (cell.isFlagged()) {
        flaggedCells += cell
      }
    }
    flaggedCells
  }

  // return [ cell for cell in theBoard if cell.isFlagged() ]
  def third(theBoard: Array[Cell]) = theBoard.filter(_.isFlagged())

}
