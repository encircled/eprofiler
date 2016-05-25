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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setStartTime(Long time) {
        start = time;
    }

    @Override
    public void end() {
        totalTime = Timer.now - start;
        if (totalTime < 10) {
            if (parent != null) {
                // add merge
            }
        } else {
            ProfilerCore.output().info(id + ":" + formatStackTrace(Thread.currentThread().getStackTrace()[2]) + ":" + totalTime);
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
        return state;
    }

}
