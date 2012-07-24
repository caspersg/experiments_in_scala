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

//   (define (cons head tail) 
//       (lambda (select) 
//           (cond (= select 1)
//               head
//               tail)))
//   (define (car x) (x 1))
//   (define (cdr x) (x 2))

object FuncList extends Application {
  type consT[H, T] = (Boolean) => Any
  def cons[H, T](head: H, tail: T): consT[H, T] = { (selectHead: Boolean) =>
    if (selectHead) {
      head
    } else {
      tail
    }
  }
  def head[H, T](list: consT[H, T]): H = list(true).asInstanceOf[H]
  def tail[H, T](list: consT[H, T]): T = list(false).asInstanceOf[T]

  val a = cons(1, cons(2, 3))
  assert(head(a) == 1)
  assert(head(tail(a)) == 2)
  assert(tail(tail(a)) == 3)
}

// (define (cons a b) (lambda (x) (x a b)))
// (define (car x) (x (lambda (a b) a)))
// (define (cdr x) (x (lambda (a b) b)))

object FuncList2 extends Application {
  type consT[H, T] = ((H, T) => Any) => Any
  def cons[H, T](head: H, tail: T): consT[H, T] = { x => x(head, tail) }
  def head[H, T](x: consT[H, T]): H = x((a: H, b: T) => a).asInstanceOf[H]
  def tail[H, T](x: consT[H, T]): T = x((a: H, b: T) => b).asInstanceOf[T]

  val a = cons(1, cons(2, 3))
  assert(head(a) == 1)
  assert(head(tail(a)) == 2)
  assert(tail(tail(a)) == 3)
}

object FuncList3 extends Application {
  type consT[H, T] = ((H, T) => Any) => Any
  def cons[H, T](head: H, tail: T): consT[H, T] = _(head, tail)
  def head[H, T](x: consT[H, T]) = x((a, _) => a).asInstanceOf[H]
  def tail[H, T](x: consT[H, T]) = x((_, b) => b).asInstanceOf[T]

  val a = cons(1, cons(2, 3))
  assert(head(a) == 1)
  assert(head(tail(a)) == 2)
  assert(tail(tail(a)) == 3)
}

