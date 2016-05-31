package cz.encircled.eprofiler.test

import net.openhft.chronicle.queue.ChronicleQueueBuilder

/**
 * @author Kisel on 25.05.2016.
 */
object LogReader {

    @JvmStatic fun main(args: Array<String>) {
        val basePath = "D:/temp/"
        val queue = ChronicleQueueBuilder.single(basePath).build()
        val tailer = queue.createTailer()

        tailer.readDocument { w -> println("msg: " + w.read { "msg" }.text()!!) }
        println("Text:")

        do {
            val s: String? = tailer.readText();
            println(s)
        } while (s != null)
    }

}
