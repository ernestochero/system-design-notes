package examples

object HelloWorld {
    def main(args: Array[String]): Unit = {
        println("Hello, World!")
        val result = sum(3, 5)
        println(s"Sum of 3 and 5 is: $result")
        val product = prod2(result)
        println(s"Product of $result and 2 is: $product")
    }
}


def sum(a: Int, b: Int): Int = a + b
def prod2(a: Int): Int = a * 2