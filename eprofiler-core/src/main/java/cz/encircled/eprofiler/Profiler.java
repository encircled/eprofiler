package cz.encircled.eprofiler;

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

    public static MethodState methodStart() {
        Agent.getWriter().info("Method start -- " + formatStackTrace(Thread.currentThread().getStackTrace()[2]));
        MethodState state = Profiler.state.get();
        if (state == null) {
            state = new DefaultMethodState();
            state.setStartTime(Timer.now);
            return state;
        } else {
            DefaultMethodState nested = new DefaultMethodState();
            nested.setStartTime(Timer.now);
            nested.parent = state;
            state.addNested(nested);
            return nested;
        }
    }

    private static String formatStackTrace(StackTraceElement traceElement) {
        return traceElement.getMethodName() + ":" + traceElement.getClassName();
    }

}
