package cz.encircled.eprofiler.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Vlad on 23-May-16.
 */
public class ClassAdapter extends ClassVisitor {

    final String owner;

    public ClassAdapter(ClassVisitor cv, final String owner) {
        super(Opcodes.ASM5, cv);
        this.owner = owner;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
                exceptions);
        if (mv != null && !name.equals("<init>")) {
            mv = new ProfilerMethodAdapter2(Opcodes.ASM5, mv);
//            mv = new ProfilerMethodAdapter(owner, mv, access, name, desc);
        }
        return mv;
    }

}
