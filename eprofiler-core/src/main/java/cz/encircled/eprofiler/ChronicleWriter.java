package cz.encircled.eprofiler;

import cz.encircled.eprofiler.core.ProfilerCore;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ChronicleQueueBuilder;
import net.openhft.chronicle.queue.ExcerptAppender;

/**
 * @author Vlad on 23-May-16.
 */
public class ChronicleWriter implements OutputWriter {

    private final boolean debug;
    private ExcerptAppender appender;

    public ChronicleWriter() {
        String basePath = "D:/temp/";
        ChronicleQueue queue = ChronicleQueueBuilder.single(basePath).build();
        appender = queue.createAppender();
        debug = ProfilerCore.config().isDebug();
    }

    public void info(String message) {
        System.out.println(message);
        appender.writeText(message);
    }

    @Override
    public void debug(String message) {
        if (debug) {
            System.out.println("profiler: " + message);
        }
    }

    @Override
    public void warn(String message) {
        System.err.println(message);
    }

}
