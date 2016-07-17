package cz.encircled.eprofiler.ui.fx.parser;

import cz.encircled.eprofiler.ui.fx.LogEntry;

import java.util.List;

/**
 * @author Vlad on 17-Jul-16.
 */
public interface LogEntryService {

    void evictEntryCache();

    List<LogEntry> getAllEntries(String path);

    List<LogEntry> getAllCallsOfMethod(String path, long methodId);

}
