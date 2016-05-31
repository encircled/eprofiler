package cz.encircled.eprofiler

import java.util.concurrent.atomic.AtomicLong

/**
 * @author Kisel on 24.05.2016.
 */
object Profiler {

    //    private static ArrayBlockingQueue<MethodState> states = new ArrayBlockingQueue<>(16);

    var state: ThreadLocal<MethodState> = object : ThreadLocal<MethodState>() {
        override fun initialValue(): MethodState? {
            return null
        }
    }
    private val idCounter = AtomicLong(1L)

    fun methodStart(): MethodState {
        var state: MethodState? = Profiler.state.get()
        if (state == null) {
            state = DefaultMethodState(0)
            state.id = idCounter.andIncrement
            state.setStartTime(Timer.now)
            Profiler.state.set(state)
            return state
        } else {
            val nested = DefaultMethodState(0)
            nested.id = idCounter.andIncrement
            nested.setStartTime(Timer.now)
            nested.parent = state
            state.addNested(nested)
            Profiler.state.set(nested)
            return nested
        }
    }

}
