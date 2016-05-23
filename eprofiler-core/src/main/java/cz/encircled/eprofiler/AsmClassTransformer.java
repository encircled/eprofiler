package cz.encircled.eprofiler;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vlad on 23-May-16.
 */
public class AsmClassTransformer implements ClassFileTransformer {

    private final Matcher classMatcher;

    public AsmClassTransformer() {
        classMatcher = Pattern.compile(Agent.getConfig().getClassNamePattern()).matcher("");
        Agent.getWriter().info("ASM transformer created");
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        String dottedClassName = className.replaceAll("/", "\\.");

        if (classMatcher.reset(dottedClassName).matches()) {
            Agent.getWriter().info("Transform " + dottedClassName);
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader, 0);
            ClassAdapter visitor = new ClassAdapter(writer);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        }

        return classfileBuffer;
    }

}
