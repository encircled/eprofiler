package cz.encircled.eprofiler.registry;

/**
 * @author Vlad on 25-Jun-16.
 */
public class MethodDescriptor {

    public long id;

    public String name;

    public String className;

    @Override
    public String toString() {
        return "MethodDescriptor{" +
                "name='" + name + '\'' +
                '}';
    }
}
