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

object Break {
  /*Map map = new TreeMap();
        for (int j = -1, i = 0; i < tokens.length; )
        {
            if (tokens[i] != null && tokens[i].length() > 0)
            {
                j = source.indexOf(tokens[i], j + 1);
                if (j >= 0)
                {
                    map.put(new Integer(j), new Integer(i));
                    continue;
                }
            }
            i++;
        }*/

  val tokens = List("some text", "other text")
  val source = "some more text"

  def something(tokens: List[String], source: String) = {
    var map = Map[Int, Int]()
    var j = -1
    var i = 0
    while (i < tokens.length) {
      val tokenAvailable = tokens(i) != null && tokens(i).size > 0
      if (tokenAvailable) {
        j = source.indexOf(tokens(i), j + 1)
      }
      if (tokenAvailable && j >= 0) {
        map += j -> i
      } else {
        i += 1
      }
    }
    map
  }

  def something2(tokens: List[String], source: String) = {
    var map = Map[Int, Int]()
    var rest = source
    tokens.zipWithIndex.filter(_._1.length > 0).foreach { i =>
      val j = rest.indexOf(i._1)
      if (j >= 0) {
        map += j -> i._2
        rest = rest.substring(j)
      }
    }
    map
  }
}