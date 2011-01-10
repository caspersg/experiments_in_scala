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

object PyVClj {

  // clojure from http://www.bestinclass.dk/index.clj/2009/10/python-vs-clojure-evolving.html

  // python
  // multiple = lambda x, y: x % y == 0
  // print sum(x for x in xrange(1, 1000+1)
  //          if multiple(x, 3) or multiple(x, 5))
  // clojure
  // (reduce +
  //        (filter #(or (zero? (rem % 3))
  //                     (zero? (rem % 5)))
  //             (range 1000)))

  (0 to 1000).filter(x =>
    x % 3 == 0 || x % 5 == 0).
    reduceLeft(_ + _)

  // clojure
  // (def fib-seq (lazy-cat [0 1]
  //      (map + fib-seq (rest fib-seq))))
  // (reduce +
  //       (filter even?
  //             (take-while #(< % 4000000)
  //                  fib-seq)))

  lazy val fib: Stream[Int] = Stream.cons(0,
    Stream.cons(1, fib.zip(fib.tail).
      map(p => p._1 + p._2)))

}