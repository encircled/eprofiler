package cz.encircled.eprofiler

/**
 * @author Vlad on 23-May-16.
 */
class Timer : Thread() {

    override fun run() {
        while (true) {
            now = System.currentTimeMillis()
            try {
                Thread.sleep(1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    companion object {

        @Volatile var now: Long = 0
    }
}
