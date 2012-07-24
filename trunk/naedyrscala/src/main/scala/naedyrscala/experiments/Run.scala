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

import java.io._

object Run {
  def apply(cmd: String): List[String] = apply(new File("."), cmd)
  def apply(dir: File, cmd: String): List[String] = {
    val process = Runtime.getRuntime.exec(cmd, null, dir)
    val resultBuffer = new BufferedReader(new InputStreamReader(process.getInputStream))
    val lines = streamToLines(resultBuffer)
    process.waitFor
    resultBuffer.close
    if (process.exitValue != 0)
      throw new RuntimeException(lines.mkString("\n"))
    else
      lines
  }

  def streamToLines(is: BufferedReader): List[String] = {
    def inner(reader: BufferedReader, lines: List[String]): List[String] = {
      val line = reader.readLine()
      if (line != null) {
        inner(reader, line :: lines)
      } else {
        lines
      }
    }
    inner(is, Nil).reverse
  }
}