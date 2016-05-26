package cz.encircled.eprofiler.asm;

import cz.encircled.eprofiler.MethodState;
import cz.encircled.eprofiler.Profiler;
import cz.encircled.eprofiler.Util;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author Vlad on 23-May-16.
 */
public class ProfilerMethodAdapter extends AdviceAdapter {

    private int index;

    protected ProfilerMethodAdapter(MethodVisitor mv, int access, String name, String desc) {
        super(Util.version, mv, access, name, desc);
    }

    @Override
    protected void onMethodEnter() {
        mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Profiler.class), "methodStart",
                Type.getMethodDescriptor(Util.getMethod(Profiler.class, "methodStart")), false);
        index = newLocal(Type.getType(MethodState.class));
        mv.visitVarInsn(Opcodes.ASTORE, index);

    }

    @Override
    protected void onMethodExit(int opcode) {
        mv.visitVarInsn(ALOAD, index);
        mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(MethodState.class), "end", Type.getMethodDescriptor(Util.getMethod(MethodState.class, "end")),
                true);
    }

}
