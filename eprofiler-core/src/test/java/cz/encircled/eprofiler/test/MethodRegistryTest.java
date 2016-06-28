package cz.encircled.eprofiler.test;

import cz.encircled.eprofiler.registry.MethodDescriptor;
import cz.encircled.eprofiler.registry.MethodRegistry;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kisel on 27.06.2016.
 */
public class MethodRegistryTest {

    @Test
    public void testMethodDescription() {
        String name = "test";
        String desc = "(Lcz/encircled/eprofiler/test/MethodRegistryTest;Ljava/lang/String;)Ljava/lang/String;";

        MethodDescriptor descriptor = MethodRegistry.register(name, "cz/test/ClassName", desc);

        Assert.assertEquals(name, descriptor.name);
        Assert.assertEquals("cz.test.ClassName", descriptor.className);
        Assert.assertEquals("MethodRegistryTest, String", descriptor.arguments);
        Assert.assertEquals("String", descriptor.returnType);
    }

}
