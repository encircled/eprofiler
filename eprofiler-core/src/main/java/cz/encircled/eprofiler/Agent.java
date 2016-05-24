package cz.encircled.eprofiler;

import java.lang.instrument.Instrumentation;

import cz.encircled.eprofiler.asm.AsmClassTransformer;

/**
 * @author Vlad on 23-May-16.
 */
public class Agent {

    public static boolean bool = false;
    private static OutWriter outWriter = new ConsoleWriter();
    private static ProfileConfiguration configuration = new ProfileConfiguration();

    static {
        configuration.validate();
    }

    public static OutWriter getWriter() {
        return outWriter;
    }

    public static ProfileConfiguration getConfig() {
        return configuration;
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        new Timer().start();
        inst.addTransformer(new AsmClassTransformer());
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception {
        new Timer().start();
        inst.addTransformer(new AsmClassTransformer());
    }

}
