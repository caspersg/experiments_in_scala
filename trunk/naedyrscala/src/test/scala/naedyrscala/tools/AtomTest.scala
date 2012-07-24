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

import org.junit._
import Assert.assertEquals

class AtomTest extends App {
  @Test
  def test1() {
    val myAtom = Atom(5)
    myAtom set 5
    assertEquals(5, {
      myAtom.get
    })
    myAtom.set(1)
    assertEquals(1, {
      myAtom.get
    })
    myAtom.set(3)
    assertEquals(3, {
      myAtom.get
    })
    myAtom.set(_ + 3)
    myAtom.set { x =>
      x + 3
    }
    assertEquals(9, {
      myAtom.get
    })
    myAtom.set(_ - 3)
    assertEquals(6, {
      myAtom.get
    })
  }

  @Test
  def incorrectUsage() {
    val myAtom = Atom(5)
    // intended to be 1
    val newValue = if (myAtom.get == 5) 1 else -1
    // newValue has already been set, therefore can't be retried
    // image if some other thread then set the value
    myAtom.set(8)
    // this set is setting the 1 we got earlier, even though the old value isn't 5 anymore
    myAtom.set(newValue)

    assertEquals(1, {
      myAtom.get
    })
  }

  @Test
  def correctUsage() {
    val myAtom = Atom(5)
    myAtom.set(8)
    // now this set is taking in to account the 8 set earlier, and we get -1 as expected
    myAtom.set(x => if (x == 5) 1 else -1)
    assertEquals(-1, {
      myAtom.get
    })
  }

  @Test
  def nestedSet() {
    val atom1 = Atom(1)
    val atom2 = Atom(2)

    // the transactions don't nest properly,
    atom1.set { x =>
      val value = atom2.set { y =>
        x + y
      }
      // if there is a collision with atom1 here, atom2 will have an inconsistent value, until atom1's set retry succeeds
      value * 2
    }

    assertEquals(6, {
      atom1.get
    })
    assertEquals(3, {
      atom2.get
    })
  }
}