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

import org.junit._
import Assert.assertEquals

object Optional extends Application {
  val a: Option[String] = Some("a")
  val b: Option[String] = None

  assertEquals(Some("a"), {
    a map (x => x)
  })

  assertEquals(None, {
    b map (x => x)
  })

  // option propagates
  assertEquals(Some("a2"), {
    a map (_ + 2)
  })
  assertEquals(None, {
    b map (_ + 2)
  })

  assertEquals(Some("a2"), {
    for (x <- a) yield x + 2
  })

  assertEquals(None, {
    for (x <- b) yield x + 2
  })

  assertEquals(Some("a2"), {
    a match {
      case Some(x: String) => Some(x + 2)
      case None => None
    }
  })

  assertEquals(None, {
    b match {
      case Some(x: String) => Some(x + 2)
      case None => None
    }
  })

  // from
  // http://speaking-my-language.blogspot.com/2010/08/why-scalas-option-wont-save-you-from.html

  case class Person(name: String, email: String)
  val employees = List(Person("bob", "bob@builder.com"))
  val occupations = Map("builder" -> employees)

  assertEquals(Some(Person("bob", "bob@builder.com")), {
    employees find (_.name == "bob")
  })

  assertEquals(Some("bob@builder.com"), {
    employees find (_.name == "bob") map (_.email)
  })

  assertEquals(Some(Some(Person("bob", "bob@builder.com"))), {
    occupations.get("builder") map { _ find (_.name == "bob") }
  })

  assertEquals(Some(Person("bob", "bob@builder.com")), {
    occupations.get("builder") flatMap { _ find (_.name == "bob") }
  })

  assertEquals(Some("bob@builder.com"), {
    occupations.get("builder") flatMap { _ find (_.name == "bob") } map (_.email)
  })

  assertEquals(None, {
    occupations.get("builder") flatMap { _ find (_.name == "jen") } map (_.email)
  })

  assertEquals("bob@builder.com", {
    occupations.get("builder").
    flatMap { _ find (_.name == "bob") }.
    map(_.email).
    filter { _ endsWith "builder.com" }.
    getOrElse("nobody@nowhere.com")
  })

  assertEquals("bob@builder.com", {
    {
      for (
        builders <- occupations.get("builder");
        bobTheBuilder <- builders find (_.name == "bob");
        email = bobTheBuilder.email if email endsWith "builder.com"
      ) yield email
    } getOrElse "nobody@nowhere.com"
  })

  assertEquals("a", {
    a getOrElse "fail"
  })

  assertEquals("fail", {
    b getOrElse "fail"
  })

  assertEquals(List(1, 2, 3, 4), {
    List(1, 2, 3) ++ Some(4) ++ None
  })

}
