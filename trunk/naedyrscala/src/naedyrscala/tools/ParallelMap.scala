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

object ParallelMap {
  // run foreach and map in parallel
  class ParallelIterable[T](seq: Iterable[T]) {
    def pforeach(f: (T) => Unit): Unit = {
      val futures = seq.map { x: T =>
        {
          Background { f(x) }
        }
      }
      futures.foreach { _.value }
    }
    def pmap[R](f: (T) => R): Iterable[R] = {
      val futures = seq.map { x: T =>
        {
          Background { f(x) }
        }
      }
      futures.map { x => x.value }
    }
    // memoize map, all elements start at the same Time, so never takes advantage of memoization
    // if max threads was restricted, then some sequential elements would use memoized values
    def mempmap[R](f: (T) => R): Iterable[R] = {
      val mf = Mem(f(_: T))
      val futures = seq.map { x: T =>
        {
          Background { mf(x) }
        }
      }
      futures.map { x => x.value }
    }
  }

  implicit def seq2ParallelIterable[T](seq: Iterable[T]) = new ParallelIterable(seq)

  object Parallel extends Application {
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

    def test(wait: Int, range: Iterable[Int]) = {
      val f1 = (x: Int) => {
        Thread.sleep(wait)
        x * 2
      }
      val sequential = Time(range map f1)
      val memoized = Time(range.map(Mem(f1(_))))
      val parallel = Time(range pmap f1)
      val memParallel = Time(range.pmap(Mem(f1(_))))
      assert(sequential._1 == parallel._1 && sequential._1 == memParallel._1 && sequential._1 == memoized._1)
      (sequential._2, memoized._2, parallel._2, memParallel._2)
    }

    val waits = List(1, 10, 100)
    val counts = List(1, 10, 100)

    val resultsD = for (
      wait <- waits;
      count <- counts
    ) yield {
      test(wait, (1 to count))
    }
    println("different data 1,10,100 sequential,memoized,parallel,memoized_parallel " + resultsD)

    val someInts = List.flatten(List.make(10, List(1, 2, 3, 4)))
    val resultsR = for (
      wait <- waits;
      count <- counts
    ) yield {
      test(wait, someInts)
    }
    println("with repeats data 1,10,100 sequential,memoized,parallel,memoized_parallel " + resultsR)
  }
}