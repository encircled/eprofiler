package cz.encircled.eprofiler.ui.fx.parser;

import cz.encircled.eprofiler.ui.fx.LogEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vlad on 17-Jul-16.
 */
public class LogEntryServiceImpl implements LogEntryService {

    private List<LogEntry> logEntries = null;

    private LogParser logParser;

    public LogEntryServiceImpl(LogParser logParser) {
        this.logParser = logParser;
    }

    @Override
    public List<LogEntry> getAllEntries(String path) {
        if (logEntries == null) {
            logEntries = logParser.parse(path);
        }
        return logEntries;
    }

    @Override
    public List<LogEntry> getAllCallsOfMethod(String path, long methodId) {
        return collectAllChildren(path)
                .parallelStream()
                .filter(logEntry -> logEntry.id == methodId)
                .collect(Collectors.toList());
    }

    @Override
    public void evictEntryCache() {

    }

    private List<LogEntry> collectAllChildren(String path) {
        ArrayList<LogEntry> target = new ArrayList<>(256);
        for (LogEntry logEntry : getAllEntries(path)) {
            collectAllChildrenInner(logEntry, target);
        }
        return target;
    }

    private void collectAllChildrenInner(LogEntry logEntry, List<LogEntry> target) {
        target.add(logEntry);
        for (LogEntry child : logEntry.children) {
            collectAllChildrenInner(child, target);
        }
    }

}
