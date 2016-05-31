package cz.encircled.eprofiler

import org.objectweb.asm.Opcodes
import java.lang.reflect.Method

/**
 * @author Vlad on 23-May-16.
 */
object Util {

    val version = Opcodes.ASM5

    fun isNotEmpty(string: String?): Boolean {
        return string != null && !string.isEmpty()
    }

    fun getMethod(clazz: Class<*>, name: String): Method {
        for (method in clazz.declaredMethods) {
            if (method.name == name) {
                return method
            }
        }
        throw RuntimeException("Method " + name + " not found on class " + clazz.name)
    }

}
