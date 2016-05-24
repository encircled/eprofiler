package cz.encircled.eprofiler.asm;

import cz.encircled.eprofiler.Agent;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
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

        if (isNotInternal(dottedClassName) && classMatcher.reset(dottedClassName).matches()) {
            Agent.getWriter().info("Transform " + dottedClassName);
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);

            TraceClassVisitor visitor = new TraceClassVisitor(writer, new PrintWriter(System.out));
            ClassAdapter classAdapter = new ClassAdapter(visitor, className);

            reader.accept(classAdapter, 0);
//            reader.accept(visitor, 0);
            return writer.toByteArray();
        }

        return classfileBuffer;
    }

    private boolean isNotInternal(String dottedClassName) {
        return !dottedClassName.startsWith("cz.encircled.eprofiler") || dottedClassName.startsWith("cz.encircled.eprofiler.test");
    }

}