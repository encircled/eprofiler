package cz.encircled.eprofiler.ui.fx;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kisel on 26.05.2016.
 */
public class LogEntry {

    public long id;

    public long methodId;

    public String methodName;

    public String returnType;

    public String className;

    public String packageName;

    public long consumedCpu;

    public long consumedMemory;

    public int threadCount;

    public long start;

    public long end;

    public long totalTime;

    public long repeats;

    public LogEntry parent;

    public List<LogEntry> children = new ArrayList<>();

    @Override
    public String toString() {
        return "LogEntry{" +
                "methodName='" + methodName + '\'' +
                '}';
    }

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

    public long getTotalTime() {
        return totalTime;
    }

}
