package cz.encircled.eprofiler.test;

import java.util.regex.Pattern;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ChronicleQueueBuilder;
import net.openhft.chronicle.queue.ExcerptTailer;

/**
 * @author Kisel on 25.05.2016.
 */
public class LogReader {

    public static void main(String[] args) {
        System.out.println(Pattern.compile("net.homecredit.*").matcher("net.homecredit.party").matches());

        String basePath = "D:/temp/";
        ChronicleQueue queue = ChronicleQueueBuilder.single(basePath).build();
        ExcerptTailer tailer = queue.createTailer();

        tailer.readDocument(w -> System.out.println("msg: " + w.read(() -> "msg").text()));
        System.out.println("Text:");

        String s;
        while ((s = tailer.readText()) != null) {
            if (!s.endsWith("in 0") && !s.endsWith("in 1")) {
                System.out.println(s);
            }
        }
    }

}
