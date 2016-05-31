package cz.encircled.eprofiler

import cz.encircled.eprofiler.core.ProfilerCore
import net.openhft.chronicle.queue.ChronicleQueueBuilder
import net.openhft.chronicle.queue.ExcerptAppender

/**
 * @author Vlad on 23-May-16.
 */
open class ChronicleWriter : OutputWriter {

    private val debug: Boolean
    private val appender: ExcerptAppender

    init {
        val basePath = "D:/temp/"
        val queue = ChronicleQueueBuilder.single(basePath).build()
        appender = queue.createAppender()
        debug = ProfilerCore.configuration.isDebug
    }

    override fun info(message: String) {
        println(message)
        appender.writeText(message)
    }

    override fun debug(message: String) {
        if (debug) {
            println("profiler: " + message)
        }
    }

    override fun warn(message: String) {
        System.err.println(message)
    }

}
