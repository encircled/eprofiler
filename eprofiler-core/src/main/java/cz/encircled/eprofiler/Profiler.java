package cz.encircled.eprofiler;

/**
 * @author Kisel on 24.05.2016.
 */
public class Profiler {

//    private static ArrayBlockingQueue<MethodState> states = new ArrayBlockingQueue<>(16);

    static ThreadLocal<MethodState> state = new ThreadLocal<MethodState>() {
        @Override
        protected MethodState initialValue() {
            return new DefaultMethodState();
        }
    };

    public static MethodState methodStart() {
        Agent.getWriter().info("Method start --");
        MethodState state = Profiler.state.get();
        state.setStartTime(Timer.now);
        return state;
    }

}
