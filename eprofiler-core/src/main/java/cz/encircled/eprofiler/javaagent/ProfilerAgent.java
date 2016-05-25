package cz.encircled.eprofiler.javaagent;

import cz.encircled.eprofiler.asm.AsmClassTransformer;
import cz.encircled.eprofiler.core.ProfilerCore;

import java.lang.instrument.Instrumentation;

/**
 * @author Vlad on 23-May-16.
 */
public class ProfilerAgent {

    public static void premain(String args, Instrumentation inst) {
        new ProfilerCore(args);
        inst.addTransformer(new AsmClassTransformer());
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception {
        new ProfilerCore(args);
        inst.addTransformer(new AsmClassTransformer());
    }

}
