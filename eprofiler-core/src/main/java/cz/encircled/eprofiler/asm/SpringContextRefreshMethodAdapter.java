package cz.encircled.eprofiler.asm;

import cz.encircled.eprofiler.Profiler;
import cz.encircled.eprofiler.Util;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author Vlad on 23-Jul-16.
 */
public class SpringContextRefreshMethodAdapter extends AdviceAdapter {

    protected SpringContextRefreshMethodAdapter(MethodVisitor mv, int access, String name, String desc) {
        super(Util.version, mv, access, name, desc);
    }

    @Override
    protected void onMethodExit(int opcode) {
        mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Profiler.class), "startProfiling",
                Type.getMethodDescriptor(Util.getMethod(Profiler.class, "startProfiling")), false);
    }

}
