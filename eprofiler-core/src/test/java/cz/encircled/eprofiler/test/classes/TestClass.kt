package cz.encircled.eprofiler.test.classes

/**
 * @author Vlad on 23-May-16.
 */
class TestClass {

    fun firstMethod() {
        println("qwe")
    }

    fun someMethod(): String {
        val ret = "returnVal"
        try {
            Thread.sleep(1000)
            yetAnotherMethod()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        anotherMethod()
        return ret
    }

    fun anotherMethod() {
        try {
            Thread.sleep(2000)
            yetAnotherMethod()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun yetAnotherMethod() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    companion object {

        init {
            testStatic()
        }

        private fun testStatic() {
            println("testStatic")
        }
    }

}
