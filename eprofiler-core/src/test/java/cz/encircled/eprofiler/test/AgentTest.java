package cz.encircled.eprofiler.test;

import com.sun.tools.attach.VirtualMachine;
import cz.encircled.eprofiler.MethodState;
import cz.encircled.eprofiler.test.classes.RecursiveLoopWithNestedCall;
import cz.encircled.eprofiler.test.classes.RecursiveSimpleLoop;
import cz.encircled.eprofiler.test.classes.TestClass;
import org.junit.Assert;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.util.concurrent.CountDownLatch;

/**
 * @author Kisel on 24.05.2016.
 */
public class AgentTest extends AbstractProfilerTest {

    static {
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        int p = nameOfRunningVM.indexOf('@');
        String pid = nameOfRunningVM.substring(0, p);

        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
//            vm.loadAgent("D:\\Soft\\eprofiler\\eprofiler-core\\target\\eprofiler-core-1.0-SNAPSHOT.jar",
//                    "classPattern=cz.encircled.eprofiler.test.classes.*;showBytecode;minDurationToLog=0");
            vm.loadAgent("E:\\Soft\\projects\\eprofiler\\eprofiler-core\\target\\eprofiler-core-1.0-SNAPSHOT.jar",
                    "classPattern=cz.encircled.eprofiler.test.classes.*;minDurationToLog=0");
            vm.detach();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Nested method call in a loop
    @Test
    public void testRecursiveLoopWithNestedCall() {
        RecursiveLoopWithNestedCall test = new RecursiveLoopWithNestedCall();
        test.loopStart();

        Assert.assertEquals(2, states.size());
        MethodState state = states.get(1);
        states.clear();

        MethodCall reference =
                new MethodCall("loopStart", 1)
                        .child(new MethodCall("loopOuter", 25)
                                .child(new MethodCall("firstLoopNested", 25).minTime(20))
                                .child(new MethodCall("secondLoopNested", 25).minTime(20)));

        assertMethodsCalls(state, reference);
    }

    // For each loop
    @Test
    public void testRecursiveSimpleLoop() {
        RecursiveSimpleLoop test = new RecursiveSimpleLoop();
        test.recursiveLoopStart(true);

        // Constructor and single method call
        Assert.assertEquals(2, states.size());
        MethodState state = states.get(1);
        states.clear();

        MethodCall reference =
                new MethodCall("recursiveLoopStart", 1)
                        .child(new MethodCall("recursiveLoopFirstNested", 1)
                                .child(new MethodCall("recursiveLoopSecondNested", 1)
                                        .child(new MethodCall("recursiveLoopStart", 50).minTime(40))));

        assertMethodsCalls(state, reference);
    }

    @Test
    public void test() throws InterruptedException {
        TestClass testClass = new TestClass();
        testClass.someMethod();
        testClass.testStatic();
        testClass.firstMethod();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            new TestClass().someMethod();
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();

        // TODO test
//        Assert.assertEquals();
    }

}
