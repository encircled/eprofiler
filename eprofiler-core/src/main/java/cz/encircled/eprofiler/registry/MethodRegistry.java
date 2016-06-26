package cz.encircled.eprofiler.registry;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;

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

        descriptor.returnType = Type.getReturnType(description).getClassName();

        idToMethod.put(descriptor.id, descriptor);

        return descriptor;
    }

    @NotNull
    private static String parseArguments(String description) {
        String arguments = "";
        for (Type type : Type.getArgumentTypes(description)) {
            String argClassName = type.getClassName();
            arguments += argClassName.substring(argClassName.lastIndexOf(".")) + ", ";
        }
        if (!arguments.isEmpty()) {
            arguments = arguments.substring(0, arguments.length() - 3);
        }
        return arguments;
    }

    public static MethodDescriptor get(long id) {
        return idToMethod.get(id);
    }

}
