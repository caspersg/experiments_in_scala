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

import naedyrscala.tools.Parallel._
import org.junit._
import Assert._

class ParallelTest {
  def test(wait: Int, range: Iterable[Int]) = {
    val f1 = (x: Int) => {
      Thread.sleep(wait)
      x * 2
    }
    val sequential = Time(range map f1)
    val memoized = Time(range.map(Mem(f1(_))))
    val parallel = Time(range pmap f1)
    val memParallel = Time(range.mempmap(f1))
    
    // assert correct
    assertEquals(sequential._1, parallel._1)
    assertEquals(sequential._1, memParallel._1)
    assertEquals(sequential._1, memoized._1)
    
    // assert performance, overhead sometimes proves below incorrect
    /*assertEquals(true, parallel._2 < sequential._2)
    assertEquals(true, parallel._2 < memoized._2)
    // ideally this would be reversed
    assertEquals(true, parallel._2 < memParallel._2)
    assertEquals(true, memoized._2 < sequential._2)
    assertEquals(true, memParallel._2 < sequential._2)*/
    
    (sequential._2, memoized._2, parallel._2, memParallel._2)
  }

  @Test
  def exampleUsage() {
    val a = List(1, 2, 3, 4)
    a map (_ * 2)
    a map (Mem(_ * 2))
    a pmap (_ * 2)
    a pmap (Mem(_ * 2))
    a mempmap (_ * 2)

    val b = Set("hi", "there", "func")
    b.map(_ * 2)
    b.pmap(_ * 2)
    b mempmap (_ * 2)

  }
  val waits = List(1, 10, 100)
  val counts = List(1, 10, 100)

  @Test
  def testPerformanceDiff() {
    val resultsD = for {
      wait <- waits
      count <- counts
    } yield {
      test(wait, (1 to count))
    }
    println("different time 1,10,100 sequential,memoized,parallel,memoized_parallel " + resultsD)

  }

  @Test
  def testPerformanceRepeats() {
    val someInts = List.fill(10)(List(1, 2, 3, 4)).flatten
    val resultsR = for {
      wait <- waits
      count <- counts
    } yield {
      test(wait, someInts)
    }
    println("with repeats time 1,10,100 sequential,memoized,parallel,memoized_parallel " + resultsR)
  }

  @Test
  def testExistsForall() {
    val c = Set(Some("hi"), None, Some("there"), Some("func"))
    List(
      { x: Option[String] => x == None },
      { x: Option[String] => x != None }).foreach { y =>
      assertEquals(c.exists(y), c.pexists(y))
      assertEquals(c.forall(y), c.pforall(y))
    }
  }
  
  @Test
  def testFilter() {
      
  }
}