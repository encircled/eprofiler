package cz.encircled.eprofiler.javaagent

import cz.encircled.eprofiler.asm.AsmClassTransformer
import cz.encircled.eprofiler.core.ProfilerCore
import java.lang.instrument.Instrumentation

/**
 * @author Vlad on 23-May-16.
 */
object ProfilerAgent {

    @JvmStatic
    fun premain(args: String, inst: Instrumentation) {
        ProfilerCore(args)
        inst.addTransformer(AsmClassTransformer())
    }

    @JvmStatic
    fun agentmain(args: String, inst: Instrumentation) {
        val profilerCore = ProfilerCore(args)
        inst.addTransformer(AsmClassTransformer())
    }

}
