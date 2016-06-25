package cz.encircled.eprofiler;

import cz.encircled.eprofiler.registry.MethodDescriptor;
import cz.encircled.eprofiler.registry.MethodRegistry;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Kisel on 24.05.2016.
 */
public class Profiler {

//    private static ArrayBlockingQueue<MethodState> states = new ArrayBlockingQueue<>(16);

    static ThreadLocal<MethodState> state = new ThreadLocal<MethodState>() {
        @Override
        protected MethodState initialValue() {
            return null;
        }
    };
    private static AtomicLong idCounter = new AtomicLong(1L);

    public static MethodState methodStart(long id) {
        MethodDescriptor descriptor = MethodRegistry.get(id);

        MethodState state = Profiler.state.get();
        if (state == null) {
            state = new MethodState();
        } else {
            MethodState sibling = findRepeatedSibling(state.children, id);
            if (sibling != null) {
                // Simple loop
                sibling.repeats++;
                Profiler.state.set(sibling);
                sibling.starts.add(Timer.now);
                return sibling;
            } else {
                MethodState nested = new MethodState();
                state.addNested(nested);
                nested.parent = state;
                state = nested;
            }
        }
        Profiler.state.set(state);
        state.executionId = idCounter.getAndIncrement();
        state.starts.add(Timer.now);
        state.descriptor = descriptor;
        return state;
    }

    public static MethodState findRepeatedSibling(List<MethodState> states, long id) {
        for (MethodState state : states) {
            if (state.descriptor.id == id) {
                return state;
            }
        }
        return null;
    }

}
