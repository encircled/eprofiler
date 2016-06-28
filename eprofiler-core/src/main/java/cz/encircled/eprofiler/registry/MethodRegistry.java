package cz.encircled.eprofiler.registry;

import java.util.HashMap;
import java.util.Map;

import cz.encircled.eprofiler.Util;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

/**
 * @author Vlad on 25-Jun-16.
 */
public class MethodRegistry {

    private static Long idCounter = 1L;

    private static Map<Long, MethodDescriptor> idToMethod = new HashMap<>(128);

    public static MethodDescriptor register(String name, String className, String description) {
        MethodDescriptor descriptor = new MethodDescriptor();
        descriptor.name = name;
        descriptor.className = className.replaceAll("/", ".");
        descriptor.id = idCounter++;

        descriptor.arguments = parseArguments(description);

        descriptor.returnType = getTokenAfterLastDot(Type.getReturnType(description).getClassName());

        idToMethod.put(descriptor.id, descriptor);

        return descriptor;
    }

    @NotNull
    private static String parseArguments(String description) {
        String arguments = "";
        for (Type type : Type.getArgumentTypes(description)) {
            String argClassName = type.getClassName();
            arguments += getTokenAfterLastDot(argClassName) + ", ";
        }
        if (!arguments.isEmpty()) {
            arguments = arguments.substring(0, arguments.length() - 2);
        }
        return arguments;
    }

    private static String getTokenAfterLastDot(String source) {
        return Util.isNotEmpty(source) ? source.substring(source.lastIndexOf(".") + 1) : "";
    }

    public static MethodDescriptor get(long id) {
        return idToMethod.get(id);
    }

}
