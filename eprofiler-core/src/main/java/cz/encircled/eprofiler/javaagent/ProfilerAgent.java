package cz.encircled.eprofiler.javaagent;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.V1_5;

import java.lang.instrument.Instrumentation;

import cz.encircled.eprofiler.asm.AsmClassTransformer;
import cz.encircled.eprofiler.core.ProfilerCore;
import org.objectweb.asm.ClassWriter;

/**
 * @author Vlad on 23-May-16.
 */
public class ProfilerAgent {

    public static void premain(String args, Instrumentation inst) {
        new ProfilerCore(args);
        inst.addTransformer(new AsmClassTransformer());
        createClass();
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception {
        new ProfilerCore(args);
        inst.addTransformer(new AsmClassTransformer());
        createClass();
    }

    private static void createClass() {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "pkg/Comparable", null, "java/lang/Object",
                new String[] {  });
//                new String[] { "pkg/Mesurable" });
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I",
                null, new Integer(-1)).visitEnd();
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I",
                null, new Integer(0)).visitEnd();
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I",
                null, new Integer(1)).visitEnd();
        cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo",
                "(Ljava/lang/Object;)I", null, null).visitEnd();
        cw.visitEnd();
        byte[] b = cw.toByteArray();
        new DynamicClassLoader().define("pkg.Comparable", b);
    }

    static class DynamicClassLoader extends ClassLoader {

        void define(String name, byte[] bytes) {
            super.defineClass(name, bytes, 0, bytes.length);
        }

    }

}
