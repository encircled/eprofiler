package cz.encircled.eprofiler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kisel on 24.05.2016.
 */
public class DefaultMethodState implements MethodState {

    long start;

    long totalTime;

    MethodState parent;

    List<MethodState> children = new ArrayList<>(4);

    @Override
    public void setStartTime(Long time) {
        start = time;
    }

    @Override
    public void end() {
        totalTime = Timer.now - start;
        Agent.getWriter().info(Thread.currentThread().getStackTrace()[1].getMethodName() + " finished in " + totalTime);
    }

    @Override
    public void addParam(String value) {

    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public void addNested(MethodState state) {
        children.add(state);
    }

}
