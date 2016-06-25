package cz.encircled.eprofiler.asm;

import cz.encircled.eprofiler.Util;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * @author Vlad on 23-May-16.
 */
public class ClassAdapter extends ClassVisitor {

    private final String owner;

    public ClassAdapter(ClassVisitor cv, final String owner) {
        super(Util.version, cv);
        this.owner = owner;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null && !name.equals("<init>") && !name.equals("<clinit>")) { // TODO configuration?
            mv = new ProfilerMethodAdapter(mv, access, name, desc, owner);
        }
        return mv;
    }

}
