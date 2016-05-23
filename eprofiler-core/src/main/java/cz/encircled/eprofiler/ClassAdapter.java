package cz.encircled.eprofiler;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Vlad on 23-May-16.
 */
public class ClassAdapter extends ClassVisitor {

    public ClassAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        mv = new MethodAdapter(Opcodes.ASM5, mv, access, name, desc);
        return mv;
    }

}
