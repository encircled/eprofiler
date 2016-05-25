package cz.encircled.eprofiler;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ChronicleQueueBuilder;
import net.openhft.chronicle.queue.ExcerptAppender;

/**
 * @author Vlad on 23-May-16.
 */
public class ConsoleWriter implements OutWriter {

    ChronicleQueue queue;
    ExcerptAppender appender;

    public ConsoleWriter() {
        String basePath = "D:/temp/";
        queue = ChronicleQueueBuilder.single(basePath).build();
        appender = queue.createAppender();
    }

    public void info(String message) {
//        System.out.println(message);
        appender.writeText(message);
    }

}
