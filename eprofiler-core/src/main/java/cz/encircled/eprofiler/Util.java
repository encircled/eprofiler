package cz.encircled.eprofiler;

import java.lang.reflect.Method;

import org.objectweb.asm.Opcodes;

/**
 * @author Vlad on 23-May-16.
 */
public class Util {

    public static final int version = Opcodes.ASM5;

    public static boolean isNotEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static Method getMethod(Class<?> clazz, String name) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new RuntimeException("Method " + name + " not found on class " + clazz.getName());
    }

}
