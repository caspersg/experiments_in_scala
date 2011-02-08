package naedyrscala.tools

/**
 * Makes a variable Bindable. This encodes the listener pattern.
 *
 */
case class Bindable[T](private var value: T) extends Ref[T] {

  val binds: Atomic[List[T => Unit]] = AtomOptimistic(List())
  def bind(f: T => Unit): Unit = {
    binds(x => f :: x)
    // run once for the current value
    f(apply())
  }

  override def apply(): T = value

  def apply(f: => T): T = {
    val newestValue = f
    value = newestValue
    binds().foreach(x => x(newestValue))
    newestValue
  }
}

object BindableTest extends Application {
  {
    val source = Bindable(1)
    var target = 2
    var target2 = 3
    println(source + "=>" + target + "=>" + target2)

    source bind (target = _)
    source.bind(target2 = _)

    source(5)

    println(source + "=>" + target + "=>" + target2)
  }

  {
    val source = Bindable(1)
    val source2 = Bindable(2)
    var target1 = 3
    println(source + "=>" + source2 + "=>" + target1)

    source bind { source2(_) }
    source2.bind(target1 = _)

    source(6)

    println(source + "=>" + source2 + "=>" + target1)
  }

}