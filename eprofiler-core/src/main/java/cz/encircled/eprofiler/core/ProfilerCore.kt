package cz.encircled.eprofiler.core

import cz.encircled.eprofiler.ChronicleWriter
import cz.encircled.eprofiler.OutputWriter
import cz.encircled.eprofiler.Timer

/**
 * @author Vlad on 25-May-16.
 */
open class ProfilerCore(args: String) {

    init {
        configuration = ProfileConfiguration(args)
        outputWriter = ChronicleWriter()
        for (classNamePattern in configuration.getClassNamePatterns()) {
            outputWriter.debug("class pattern " + classNamePattern)
        }

        Timer().start()
    }

    companion object Runtime {

        @JvmStatic
        lateinit var outputWriter: OutputWriter;

        @JvmStatic
        lateinit var configuration: ProfileConfiguration;

    }

}
