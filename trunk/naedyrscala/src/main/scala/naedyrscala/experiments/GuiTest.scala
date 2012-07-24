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

import scala.swing._

import swing._
import event._

object Converter extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Converter"
    resizable = false
    
    object dollars extends TextField { columns = 4; text = "0" }
    object pounds extends TextField { columns = 4; text = "0" }
    object convert extends Button { text = "a button" }
    object clicked extends Label { text = "clicked 0 times" }

    contents = new FlowPanel {
      contents += dollars
      contents += new Label("dollars")
      contents += pounds
      contents += new Label("pounds")
      contents += convert
      contents += clicked
      border = Swing.EmptyBorder(5, 5, 5, 5)
    }

    listenTo(dollars, pounds, convert)

    var clickCount = 0

    reactions += {
      case EditDone(`pounds`) =>
        dollars.text = "%1.2f".format(pounds.text.toDouble * 1.55255)

      case EditDone(`dollars`) =>
        pounds.text = "%1.2f".format(dollars.text.toDouble / 1.55255)

      case ButtonClicked(b) =>
        clickCount += 1
        clicked.text = "clicked " + clickCount + " times"
        pack
    }
  }
}
