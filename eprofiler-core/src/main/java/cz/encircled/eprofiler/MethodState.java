package cz.encircled.eprofiler;

import cz.encircled.eprofiler.core.ProfilerCore;
import cz.encircled.eprofiler.registry.MethodDescriptor;
import sun.management.ManagementFactoryHelper;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

import static cz.encircled.eprofiler.Profiler.state;

/**
 * @author Kisel on 24.05.2016.
 */
public class MethodState implements MethodEnd {

    public long executionId;

    public long cpuBefore;

    public long consumedCpu = 0L;

    public long consumedMemory = 0L;

    public MethodDescriptor descriptor;

    public List<Long> starts = new ArrayList<>(4);

    public List<Long> ends = new ArrayList<>(4);

    public MethodState parent;

    public List<MethodState> children = new ArrayList<>(4);

    // how many times this method has been called in current call tree
    public long repeats = 1;

    @Override
    public void end() {
        try {
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            consumedCpu += threadMXBean.getCurrentThreadCpuTime();
        } catch (Exception e) {
            consumedCpu = 0L;
        }
        try {
            long memory = ((com.sun.management.ThreadMXBean) ManagementFactoryHelper.getThreadMXBean())
                    .getThreadAllocatedBytes(Thread.currentThread().getId());
            if (memory > consumedMemory) {
                consumedMemory = memory;
            }
        } catch (Exception e) {
            consumedMemory = 0L;
        }

        ends.add(Timer.now);

        if (hasParent()) {
            state.set(parent);
        } else {
            state.remove();
            ProfilerCore.output().flush(this);
        }
    }

    public void addParam(String value) {

    }

    public Long totalTime() {
        Long total = 0L;
        for (int i = 0; i < ends.size(); i++) {
            total += ends.get(i) - starts.get(i);
        }
        return total;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public MethodState addNested(MethodState state) {
        children.add(state);
        state.parent = this;
        return state;
    }

    @Override
    public String toString() {
        return descriptor.name;
    }
}
