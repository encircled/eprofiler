package cz.encircled.eprofiler.asm;

import cz.encircled.eprofiler.Util;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * @author Vlad on 23-Jul-16.
 */
public class SpringContextRefreshClassAdapter extends ClassVisitor {

    public SpringContextRefreshClassAdapter(ClassVisitor cv) {
        super(Util.version, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null && name.equals("finishRefresh")) {
            mv = new SpringContextRefreshMethodAdapter(mv, access, name, desc);
        }
        return mv;
    }

}

