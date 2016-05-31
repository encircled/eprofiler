package cz.encircled.eprofiler

import cz.encircled.eprofiler.core.ProfilerCore
import java.util.*

/**
 * @author Kisel on 24.05.2016.
 */
class DefaultMethodState(override var id: Long) : MethodState {

    override var parent: MethodState? = null

    internal var start: Long = 0

    internal var totalTime: Long = 0

    internal var children: MutableList<MethodState> = ArrayList(4)

    private fun formatStackTrace(traceElement: StackTraceElement): String {
        return traceElement.methodName + "#" + traceElement.className
    }

    override fun setStartTime(time: Long?) {
        start = time!!
    }

    override fun end() {
        totalTime = Timer.now - start
        if (totalTime < ProfilerCore.configuration.minDurationToLog && parent != null) {
            // add merge

        } else {
            val parentId = if (parent == null) "" else java.lang.Long.toString(parent!!.id)
            ProfilerCore.outputWriter.info("$id : $parentId : ${formatStackTrace(Thread.currentThread().stackTrace[2])} : $totalTime")
        }

        if (hasParent()) {
            Profiler.state.set(parent)
        } else {
            Profiler.state.remove()
        }
    }

    override fun addParam(value: String) {

    }

    override fun hasParent(): Boolean {
        return parent != null
    }

    override fun addNested(state: MethodState): MethodState {
        children.add(state)
        state.parent = this
        return state
    }

}
