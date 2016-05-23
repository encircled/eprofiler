package cz.encircled.eprofiler;

/**
 * @author Vlad on 23-May-16.
 */
public class ConsoleWriter implements OutWriter {

    public void info(String message) {
        System.out.println(message);
    }

}
