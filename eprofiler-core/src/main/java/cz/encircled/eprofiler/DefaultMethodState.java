package cz.encircled.eprofiler;

import cz.encircled.eprofiler.core.ProfilerCore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kisel on 24.05.2016.
 */
public class DefaultMethodState implements MethodState {

    long id;

    long start;

    long totalTime;

    MethodState parent;

    List<MethodState> children = new ArrayList<>(4);

    private static String formatStackTrace(StackTraceElement traceElement) {
        return traceElement.getMethodName() + "#" + traceElement.getClassName();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public MethodState getParent() {
        return parent;
    }

    @Override
    public void setParent(MethodState parent) {
        this.parent = parent;
    }

    @Override
    public void setStartTime(Long time) {
        start = time;
    }

    @Override
    public void end() {
        totalTime = Timer.now - start;
        if (totalTime < ProfilerCore.config().getMinDurationToLog() && parent != null) {
            // add merge

        } else {
            String parentId = parent == null ? "" : Long.toString(parent.getId());
            ProfilerCore.output().info(id + ":" + parentId + ":" + formatStackTrace(Thread.currentThread().getStackTrace()[2]) + ":" + totalTime);
        }

        if (hasParent()) {
            Profiler.state.set(parent);
        } else {
            Profiler.state.remove();
        }
    }

    @Override
    public void addParam(String value) {

    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public MethodState addNested(MethodState state) {
        children.add(state);
        state.setParent(this);
        return state;
    }

}
