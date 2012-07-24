package naedyrscala.tools

/**
 * defines a reference to a mutable value. Is this the State monad?
 *
 */
trait Ref[T] {
  /**
   * @return the current value
   */
  def apply(): T
  /**
   * set a new value, that's derived from the current value
   * @param f function which calculates the new value
   * @return the value that was set (not the pre or post value, the one set from this invocation)
   */
  def apply(f: T => T): T
  /**
   * set a new value, override and ignores current value
   * @param f function which calculates the new value
   * @return the value that was set (not the pre or post value, the one set from this invocation)
   */
  def apply(f: => T): T = apply(previous => f)
}