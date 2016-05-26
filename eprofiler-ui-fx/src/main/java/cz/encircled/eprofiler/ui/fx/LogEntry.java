package cz.encircled.eprofiler.ui.fx;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kisel on 26.05.2016.
 */
public class LogEntry {

    public long id;

    public String name;

    public long time;

    public LogEntry parent;

    public List<LogEntry> children = new ArrayList<>();

}
