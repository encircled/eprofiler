package cz.encircled.eprofiler.output;

import cz.encircled.eprofiler.MethodState;

/**
 * @author Vlad on 23-May-16.
 */
public interface OutputWriter {

    void debug(String message);

    void warn(String message);

    void info(String message);

    void flush(MethodState root);
}
