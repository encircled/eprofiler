package cz.encircled.eprofiler.ui.fx.parser;

import cz.encircled.eprofiler.ui.fx.LogEntry;

import java.util.List;

/**
 * @author Vlad on 25-Jun-16.
 */
public interface LogParser {

    List<LogEntry> parse(String path);

}
