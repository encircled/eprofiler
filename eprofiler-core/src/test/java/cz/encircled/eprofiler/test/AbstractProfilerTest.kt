package cz.encircled.eprofiler.test

import cz.encircled.eprofiler.MethodState
import cz.encircled.eprofiler.Profiler

/**
 * @author Kisel on 26.05.2016.
 */
abstract class AbstractProfilerTest {

    protected val rootState: MethodState?
        @SuppressWarnings("unchecked")
        get() {
            val holder = getFieldValue(Profiler::class.java, "state", true) as ThreadLocal<MethodState>
            var state: MethodState? = holder.get()
            if (state != null) {
                while (state!!.hasParent()) {
                    state = state.parent
                }
            }
            return state
        }

    protected fun getFieldValue(clazz: Class<*>, name: String, isStatic: Boolean): Any {
        try {
            val field = clazz.getDeclaredField(name)
            field.isAccessible = true
            return field.get(if (isStatic) null else clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Field not found $name on $clazz")
        }

    }

}