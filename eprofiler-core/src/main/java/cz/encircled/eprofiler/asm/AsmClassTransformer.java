package cz.encircled.eprofiler.asm;

import cz.encircled.eprofiler.core.ProfilerCore;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Vlad on 23-May-16.
 */
public class AsmClassTransformer implements ClassFileTransformer {

    private List<Matcher> classMatchers;

    public AsmClassTransformer() {
        classMatchers = ProfilerCore.config().getClassNamePatterns().stream()
                .map(pattern -> Pattern.compile(pattern).matcher(""))
                .collect(Collectors.toList());

        ProfilerCore.output().debug("ASM transformer created");
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        String dottedClassName = className.replaceAll("/", "\\.");

        if (dottedClassName.equals("org.springframework.context.support.AbstractApplicationContext")) {
            ProfilerCore.output().debug("Subscribing to Spring context refresh event");

            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
            ClassVisitor rootVisitor = new SpringContextRefreshClassAdapter(writer);
            reader.accept(rootVisitor, ClassReader.EXPAND_FRAMES);

            classfileBuffer = writer.toByteArray();
        }

        if (isNotInternal(dottedClassName) && matchesPattern(dottedClassName)) {
            ProfilerCore.output().debug("Transform " + dottedClassName);

            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);

            ClassVisitor rootVisitor;
            if (ProfilerCore.config().isShowBytecode()) {
                TraceClassVisitor visitor = new TraceClassVisitor(writer, new PrintWriter(System.out));
                rootVisitor = new ProfilerClassAdapter(visitor, className);
            } else {
                rootVisitor = new ProfilerClassAdapter(writer, className);
            }

            reader.accept(rootVisitor, ClassReader.EXPAND_FRAMES);
            return writer.toByteArray();
        }


        return classfileBuffer;
    }

    private boolean isNotInternal(String dottedClassName) {
        return !dottedClassName.startsWith("cz.encircled.eprofiler") || dottedClassName.startsWith("cz.encircled.eprofiler.test") || dottedClassName.startsWith("cz.encircled.eprofiler.ui");
    }

    private boolean matchesPattern(String dottedClassName) {
        for (Matcher classMatcher : classMatchers) {
            if (classMatcher.reset(dottedClassName).matches()) {
                return true;
            }
        }

        return false;
    }

}
