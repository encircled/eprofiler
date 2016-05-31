package cz.encircled.eprofiler.test

import com.sun.tools.attach.VirtualMachine
import cz.encircled.eprofiler.test.classes.TestClass
import org.junit.Before
import org.junit.Test
import java.lang.management.ManagementFactory

/**
 * @author Kisel on 24.05.2016.
 */
class AgentTest : AbstractProfilerTest() {

    @Before
    fun initializeAgent() {
        val nameOfRunningVM = ManagementFactory.getRuntimeMXBean().name
        val p = nameOfRunningVM.indexOf('@')
        val pid = nameOfRunningVM.substring(0, p)

        try {
            val vm = VirtualMachine.attach(pid)
            vm.loadAgent("D:\\Soft\\eprofiler\\eprofiler-core\\target\\eprofiler-core-1.0-SNAPSHOT.jar",
                    "classPattern=cz.encircled.eprofiler.test.classes.*;debug;showBytecode;minDurationToLog=0")
            //            vm.loadAgent("E:\\\\Soft\\\\projects\\\\eprofiler\\\\eprofiler-core\\\\target\\\\eprofiler-core-1.0-SNAPSHOT.jar",
            //                    "classPattern=cz.encircled.eprofiler.test.classes.*;showBytecode;minDurationToLog=0");
            vm.detach()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    @Test
    fun test() {
        val testClass = TestClass()
        println(testClass.someMethod())
        val state = rootState
        //        Assert.assertEquals();
    }

}
