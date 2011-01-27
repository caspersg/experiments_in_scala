// from https://gist.github.com/248807#file_string_interpolation.scalai

package naedyrscala.tools

trait InterpolationContext {
  implicit def str2interp(s: String) = new InterpolatedString(s)

  class InterpolatedString(val s: String) {
    def i = interpolate(s)
    def / = interpolate(s)
    def identifier = s.substring(2, s.length - 1)
  }

  object Tokenizer {
    def unapply(s: String): Option[Iterator[String]] = {
      Some("\\$\\{.+?\\}".r.findAllIn(s).matchData.map { (x) => x.matched })
    }
  }

  def interpolate(s: String) = {
    var sb = s
    def evaluate(token: String): String = {
      try {
        val method = this.getClass.getMethod(token.identifier)
        method.invoke(this).toString
      } catch {
        case _: NoSuchMethodException => token
      }
    }
    s match {
      case Tokenizer(tokens) =>
        tokens.foreach { (x: String) =>
          sb = sb.replace(x, evaluate(x))
        }
    }
    sb
  }
}