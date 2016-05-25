package cz.encircled.eprofiler;

import java.lang.instrument.Instrumentation;

import cz.encircled.eprofiler.asm.AsmClassTransformer;

/**
 * @author Vlad on 23-May-16.
 */
public class ProfilerAgent {

    private static OutWriter outWriter = new ConsoleWriter();
    private static ProfileConfiguration configuration;

    public static OutWriter getWriter() {
        return outWriter;
    }

    public static ProfileConfiguration getConfig() {
        return configuration;
    }

    public static void premain(String args, Instrumentation inst) {
        System.out.println("ARGS " + args);
        initConfig(args);
        new Timer().start();
        inst.addTransformer(new AsmClassTransformer());
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception {
        System.out.println("ARGS " + args);
        initConfig(args);
        new Timer().start();
        inst.addTransformer(new AsmClassTransformer());
    }

    private static void initConfig(String args) {
        configuration = new ProfileConfiguration();
        if (args != null) {
            String[] singleArgs = args.split(";");
            for (String singleArg : singleArgs) {
                String[] keyValue = singleArg.split("=");
                if (keyValue.length == 2) {
                    if (keyValue[0].equals("classPattern")) {
                        configuration.setClassNamePattern(keyValue[1]);
                    }
                } else if (keyValue.length == 1) {
                    if (keyValue[0].equals("debug")) {
                        configuration.setDebug(true);
                    }
                }
            }
        }
        configuration.validate();
    }

}
