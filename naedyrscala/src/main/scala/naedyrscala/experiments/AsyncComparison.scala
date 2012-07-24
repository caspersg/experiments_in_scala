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

import naedyrscala.tools.Result
import naedyrscala.tools.Async._

// C# from http://blogs.msdn.com/b/ericlippert/archive/2010/10/28/asynchrony-in-c-5-part-one.aspx

object AsyncComparison {
  case class Url(num: Int)
  def fetch(url: Url): Int = {
    println("fetched " + url);
    url.num
  }
  def archive(num: Int): Unit = println("archived " + num)

  // C# sync
  // void ArchiveDocuments(List<Url> urls) {
  //   for(int i = 0; i < urls.Count; ++i)
  //	     Archive(Fetch(urls[i]));
  // }

  // scala sync
  def archiveDocuments(urls: List[Url]): Unit = {
    urls.foreach { x => archive(fetch(x)) }
  }

  // C# async
  // async void ArchiveDocuments(List<Url> urls)
  // {
  //   Task archive = null;
  //   for(int i = 0; i < urls.Count; ++i)
  //   {
  //	     var document = await FetchAsync(urls[i]);
  //	     if (archive != null)
  //	       await archive;
  //	     archive = ArchiveAsync(document);
  //   }
  // }

  // scala async
  // uses the same fetch and archive methods from sync version
  def archiveDocumentsAsync(urls: List[Url]): Unit = {
    var last: Option[Result[Any]] = None
    urls.foreach { x =>
      val fetched = async(fetch(x))
      last.map(await)
      last = Some(async(archive(await(fetched))))
    }
  }

  def archiveDocumentsAsync2(urls: List[Url]): Unit = {
    urls.foreach { x =>
      async(archive(await(async(fetch(x)))))
    }
  }

  //test it
  def run() = {
    val test = List(Url(1), Url(2), Url(3))
    println("sync")
    archiveDocuments(test)

    println("async")
    archiveDocumentsAsync(test)

    println("async")
    archiveDocumentsAsync2(test)
  }
}
