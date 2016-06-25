package cz.encircled.eprofiler.ui.fx;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kisel on 26.05.2016.
 */
public class LogEntry {

    public long id;

    public String methodName;

    public String className;

    public long start;

    public long end;

    public long totalTime;

    public long repeats;

    public LogEntry parent;

    public List<LogEntry> children = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntry logEntry = (LogEntry) o;

        return id == logEntry.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

}
