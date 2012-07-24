package naedyrscala.experiments

object PalindromeTest extends App {
  def isPal[T](x: List[T]): Boolean = {
    if (x.length <= 1) true
    else if (x.head == x.last) isPal(x.tail.init)
    else false
  }
  
  def isPal2[T](x: List[T]): Boolean = x.reverse == x
  
  isPal("abba".toList) == isPal2("abba".toList)

  isPal("".toList)

  isPal("a".toList)

  isPal("abbab".toList)

}
