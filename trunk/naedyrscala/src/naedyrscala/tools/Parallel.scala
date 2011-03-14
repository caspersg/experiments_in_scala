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

import naedyrscala.tools.Async._

/**
 * I know parallel collections are coming in 2.9; I'm just having fun
 *
 */
object Parallel {
  // run foreach and map etc in parallel
  class ParallelIterable[T](seq: Iterable[T]) {
    def mapAsync[R](f: T => R) = {
      seq.map { x: T => async { f(x) } }
    }
    def pforeach(f: T => Unit): Unit = {
      mapAsync(f).foreach { _.await }
    }
    def pmap[R](f: T => R): Iterable[R] = {
      mapAsync(f).map { _.await }
    }
    // memoize map, all elements start at the same Time, so never takes advantage of memoization
    // if max threads was restricted, then some sequential elements would use memoized values
    def mempmap[R](f: T => R): Iterable[R] = {
      val mf = Mem(f(_: T))
      mapAsync(mf).map { _.await }
    }

    def pfind(f: T => Boolean): Option[T] = {
      pfilter(f) match {
        case x :: xs => Some(x)
        case Nil => None
      }
    }

    def pexists(f: T => Boolean): Boolean = {
      mapAsync(f).exists { _.await }
    }
    def pforall(f: T => Boolean): Boolean = {
      mapAsync(f).forall { _.await }
    }
    def pfilter(p: T => Boolean): Iterable[T] = {
      seq.map { x: T =>
        async {
          if (p(x)) Some(x) else None
        }
      }.map(_.await).flatMap(x => x map (y => y))
    }
  }

  implicit def seq2ParallelIterable[T](seq: Iterable[T]) = new ParallelIterable(seq)
}