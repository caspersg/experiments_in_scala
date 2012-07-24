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

// liftM2 :: Monad m => (a -> b -> c) -> m a -> m b -> m c
// liftM2 op mx my = do
//     x <- mx
//     y <- my
//     return (op x y)
// (.*.) :: Monad m => m Float -> m Float -> m Float
// x .*. y = liftM2 (*) x y

object Lift {
  def apply[X, Y, Z](op: (X, Y) => Z, mx: Option[X], my: Option[Y]): Option[Z] = {
    for {
      x <- mx;
      y <- my
    } yield op(x, y)
  }
}

object TestLift extends Application {
  val a = Some(2)
  val b = Some(3)
  val mult = (x: Int, y: Int) => x * y
  // mult(a,b) not possible
  val lmult = Lift(mult, _: Option[Int], _: Option[Int])
  assert(Some(6) == lmult(a, b))
}