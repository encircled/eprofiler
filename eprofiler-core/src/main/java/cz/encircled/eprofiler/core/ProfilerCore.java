package cz.encircled.eprofiler.core;

import cz.encircled.eprofiler.ChronicleWriter;
import cz.encircled.eprofiler.OutputWriter;
import cz.encircled.eprofiler.Timer;

/**
 * @author Vlad on 25-May-16.
 */
public class ProfilerCore {

    private static OutputWriter outputWriter = new ChronicleWriter();

    private static ProfileConfiguration configuration;

    public ProfilerCore(String args) {
        new Timer().start();
        initConfig(args);

        for (String classNamePattern : config().getClassNamePatterns()) {
            ProfilerCore.output().debug("class pattern " + classNamePattern);
        }
    }

    public static OutputWriter output() {
        return outputWriter;
    }

    public static ProfileConfiguration config() {
        return configuration;
    }

    private void initConfig(String args) {
        configuration = new ProfileConfiguration(args);
    }

}
