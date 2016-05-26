package cz.encircled.eprofiler;

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

    public static MethodState methodStart() {
        MethodState state = Profiler.state.get();
        if (state == null) {
            state = new DefaultMethodState();
            state.setId(idCounter.getAndIncrement());
            state.setStartTime(Timer.now);
            Profiler.state.set(state);
            return state;
        } else {
            DefaultMethodState nested = new DefaultMethodState();
            nested.setId(idCounter.getAndIncrement());
            nested.setStartTime(Timer.now);
            nested.parent = state;
            state.addNested(nested);
            Profiler.state.set(nested);
            return nested;
        }
    }

}
