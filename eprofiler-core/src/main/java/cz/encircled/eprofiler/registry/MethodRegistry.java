package cz.encircled.eprofiler.registry;

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

        idToMethod.put(descriptor.id, descriptor);

        return descriptor;
    }

    public static MethodDescriptor get(long id) {
        return idToMethod.get(id);
    }

}
