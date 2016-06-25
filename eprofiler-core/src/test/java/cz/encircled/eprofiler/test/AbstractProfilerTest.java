package cz.encircled.eprofiler.test;

import cz.encircled.eprofiler.MethodState;
import cz.encircled.eprofiler.core.ProfilerCore;
import cz.encircled.eprofiler.output.ChronicleWriter;
import org.junit.Assert;
import org.junit.Before;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Kisel on 26.05.2016.
 */
public abstract class AbstractProfilerTest {

    protected static List<MethodState> states = new ArrayList<>();

    @Before
    public void beforeClass() {
        try {
            Field writerField = ProfilerCore.class.getDeclaredField("outputWriter");
            writerField.setAccessible(true);
            writerField.set(null, new TestWriter());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    void assertMethodsCalls(MethodState state, MethodCall reference) {
        Assert.assertEquals(state.descriptor.name, reference.name);
        Assert.assertEquals(state.children.size(), reference.children.size());
        Assert.assertEquals(state.repeats, reference.repeats);
        Assert.assertTrue("Children total time is greater than parent", state.totalTime() >= totalChildrenTime(state));
        if (reference.minTime > 0) {
            Assert.assertTrue("Min time is " + reference.minTime + ", actual is " + state.totalTime(), state.totalTime() > reference.minTime);
        }

        for (int i = 0; i < reference.children.size(); i++) {
            assertMethodsCalls(state.children.get(i), reference.children.get(i));
        }
    }

    long totalChildrenTime(MethodState state) {
        long total = 0L;
        for (MethodState child : state.children) {
            total += child.totalTime();
        }
        return total;
    }

    private static class TestWriter extends ChronicleWriter {

        private void printOrdered(MethodState root) {
            TreeSet<MethodState> all = new TreeSet<>((o1, o2) -> {
                return Long.compare(o1.executionId, o2.executionId);
            });
            collectAll(root, all);
            for (MethodState methodState : all) {
                System.out.println(buildMessage(methodState, methodState.totalTime()));
            }
        }

        private void collectAll(MethodState root, TreeSet<MethodState> collect) {
            collect.add(root);
            for (MethodState child : root.children) {
                collectAll(child, collect);
            }
        }

        @Override
        public void flush(MethodState root) {
            printOrdered(root);
            states.add(root);
            super.flush(root);
        }

    }

    class MethodCall {

        String name;

        long repeats;

        long minTime = -1L;

        List<MethodCall> children = new ArrayList<>();

        MethodCall(String name, long repeats) {
            this.name = name;
            this.repeats = repeats;
        }

        MethodCall minTime(long time) {
            this.minTime = time;
            return this;
        }

        MethodCall child(MethodCall child) {
            children.add(child);
            return this;
        }

    }

}