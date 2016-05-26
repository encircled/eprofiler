package cz.encircled.eprofiler.test;

import java.lang.reflect.Field;

import cz.encircled.eprofiler.MethodState;
import cz.encircled.eprofiler.Profiler;

/**
 * @author Kisel on 26.05.2016.
 */
public abstract class AbstractProfilerTest {

    @SuppressWarnings("unchecked")
    protected MethodState getRootState() {
        ThreadLocal<MethodState> holder = (ThreadLocal<MethodState>) getFieldValue(Profiler.class, "state", true);
        MethodState state = holder.get();
        if (state != null) {
            while (state.hasParent()) {
                state = state.getParent();
            }
        }
        return state;
    }

    protected Object getFieldValue(Class<?> clazz, String name, boolean isStatic) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(isStatic ? null : clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Field not found " + name + " on " + clazz);
        }
    }

}