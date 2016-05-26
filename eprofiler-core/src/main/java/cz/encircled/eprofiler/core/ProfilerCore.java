package cz.encircled.eprofiler.core;

import cz.encircled.eprofiler.ChronicleWriter;
import cz.encircled.eprofiler.OutputWriter;
import cz.encircled.eprofiler.Timer;

/**
 * @author Vlad on 25-May-16.
 */
public class ProfilerCore {

    private static OutputWriter outputWriter;

    private static ProfileConfiguration configuration;

    public ProfilerCore(String args) {
        configuration = new ProfileConfiguration(args);
        outputWriter = new ChronicleWriter();

        for (String classNamePattern : config().getClassNamePatterns()) {
            ProfilerCore.output().debug("class pattern " + classNamePattern);
        }
        new Timer().start();
    }

    public static OutputWriter output() {
        return outputWriter;
    }

    public static ProfileConfiguration config() {
        return configuration;
    }

}
