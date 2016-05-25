package cz.encircled.eprofiler;

/**
 * @author Vlad on 23-May-16.
 */
public interface OutputWriter {

    void debug(String message);

    void warn(String message);

    void info(String message);

}
