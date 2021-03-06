package cz.encircled.eprofiler.test;

import cz.encircled.eprofiler.MethodState;
import cz.encircled.eprofiler.test.classes.RecursiveLoopWithNestedCall;
import cz.encircled.eprofiler.test.classes.RecursiveSimpleLoop;
import cz.encircled.eprofiler.test.classes.TestClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author Kisel on 24.05.2016.
 */
public class AgentTest extends AbstractProfilerTest {

    @Before
    public void before() {
        states.clear();
        /*try {
            String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
            int p = nameOfRunningVM.indexOf('@');
            long pid = Long.parseLong(nameOfRunningVM.substring(0, p));

            Sigar sigar = new Sigar();
            ProcCpu procCpu = new ProcCpu();
            procCpu.gather(sigar, pid);

            ProcMem memory = new ProcMem();
            memory.gather(sigar, pid);
            System.out.println(Long.toString(memory.getSize()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }*/
    }

    // Nested method call in a loop
    @Test
    public void testRecursiveLoopWithNestedCall() {
        RecursiveLoopWithNestedCall test = new RecursiveLoopWithNestedCall();
        test.loopStart();

        Assert.assertEquals(1, states.size());
        MethodState state = states.get(0);

        MethodCall reference =
                new MethodCall("loopStart", 1)
                        .child(new MethodCall("loopOuter", 20)
                                .child(new MethodCall("firstLoopNested", 20).minTime(20))
                                .child(new MethodCall("secondLoopNested", 20).minTime(20)));

        assertMethodsCalls(state, reference);
    }

    // For each loop
    @Test
    public void testRecursiveSimpleLoop() {
        RecursiveSimpleLoop test = new RecursiveSimpleLoop();
        test.recursiveLoopStart(true);

        // Constructor and single method call
        Assert.assertEquals(1, states.size());
        MethodState state = states.get(0);
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
    }


}
