package cz.encircled.eprofiler;

import java.lang.reflect.Method;

import org.objectweb.asm.Opcodes;

/**
 * @author Vlad on 23-May-16.
 */
public class Util {

    public static final int version = Opcodes.ASM4;

    public static Method getMethod(Class<?> clazz, String name) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new RuntimeException("Method " + name + " not found on class " + clazz.getName());
    }

    public void asm() {
        try {
            ProfilerAgent.getWriter().info("start");
            getMethod(Util.class, "getMethod");
        } finally {
            ProfilerAgent.getWriter().info("end");
        }

    }

}
