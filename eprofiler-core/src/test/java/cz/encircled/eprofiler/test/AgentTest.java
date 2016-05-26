package cz.encircled.eprofiler.test;

import com.sun.tools.attach.VirtualMachine;
import cz.encircled.eprofiler.MethodState;
import cz.encircled.eprofiler.test.classes.TestClass;
import org.junit.Before;
import org.junit.Test;

import java.lang.management.ManagementFactory;

/**
 * @author Kisel on 24.05.2016.
 */
public class AgentTest extends AbstractProfilerTest {

    @Before
    public void initializeAgent() {
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        int p = nameOfRunningVM.indexOf('@');
        String pid = nameOfRunningVM.substring(0, p);

        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
//            vm.loadAgent("D:\\Soft\\eprofiler\\eprofiler-core\\target\\eprofiler-core-1.0-SNAPSHOT.jar",
//                    "classPattern=cz.encircled.eprofiler.test.classes.*;showBytecode;minDurationToLog=0");
            vm.loadAgent("E:\\\\Soft\\\\projects\\\\eprofiler\\\\eprofiler-core\\\\target\\\\eprofiler-core-1.0-SNAPSHOT.jar",
                    "classPattern=cz.encircled.eprofiler.test.classes.*;showBytecode;minDurationToLog=0");
            vm.detach();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test() {
        TestClass testClass = new TestClass();
        System.out.println(testClass.someMethod());
        MethodState state = getRootState();
//        Assert.assertEquals();
    }

}
