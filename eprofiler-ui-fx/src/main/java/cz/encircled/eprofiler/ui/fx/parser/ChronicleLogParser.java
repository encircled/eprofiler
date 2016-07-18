package cz.encircled.eprofiler.ui.fx.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.encircled.eprofiler.ui.fx.LogEntry;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ChronicleQueueBuilder;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.TailerDirection;

/**
 * @author Vlad on 25-Jun-16.
 */
public class ChronicleLogParser implements LogParser {

    @Override
    public List<LogEntry> parse(String path) {
        return parseInternal(path);
    }

    private List<LogEntry> parseInternal(String path) {
        List<LogEntry> roots = new ArrayList<>(64);
        Map<Long, LogEntry> index = new HashMap<>(256);

        ExcerptTailer tailer = getExcerptTailer(path);

        String s;
        while ((s = tailer.readText()) != null) {
            String[] split = s.split(":");
            LogEntry entry = new LogEntry();
            entry.id = Long.parseLong(split[0]);
            entry.methodId = Long.parseLong(split[2]);
            entry.methodName = split[3] + "(" + split[4] + ")";
            entry.returnType = split[5];

            entry.className = split[6].substring(split[6].lastIndexOf("."));
            entry.packageName = split[6].substring(0, split[6].lastIndexOf("."));

            entry.start = Long.parseLong(split[7]);
            entry.end = Long.parseLong(split[8]);
            entry.totalTime = Long.parseLong(split[9]);
            entry.repeats = Long.parseLong(split[10]);
            entry.consumedCpu = Long.parseLong(split[11]);
            entry.consumedMemory = Long.parseLong(split[12]);
            entry.threadCount = Integer.parseInt(split[13]);

            if (split[1].isEmpty()) {
                roots.add(entry);
            } else {
                LogEntry parentEntry = index.get(Long.parseLong(split[1]));
                parentEntry.children.add(entry);
                entry.parent = parentEntry;
            }
            index.put(entry.id, entry);
        }
        return roots;
    }

    private ExcerptTailer getExcerptTailer(String path) {
        ChronicleQueue queue = ChronicleQueueBuilder.single(path).build();
        ExcerptTailer tailer = queue.createTailer();
        tailer.direction(TailerDirection.BACKWARD);
        tailer.toEnd();
        return tailer;
    }

}
