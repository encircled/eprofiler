package cz.encircled.eprofiler.asm;

import cz.encircled.eprofiler.Agent;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author Vlad on 23-May-16.
 */
public class MethodAdapter extends AdviceAdapter {

    final String methodName;

    final String owner;

    protected MethodAdapter(String owner, MethodVisitor mv, int access, String name, String desc) {
        super(ASM5, mv, access, name, desc);
        this.methodName = name;
        this.owner = owner;
        Agent.getWriter().info("Method adapter for " + methodName + " created.");
    }

    @Override
    protected void onMethodEnter() {
        Agent.getWriter().info("OnMethodEnter: " + methodName + " on " + owner);
        mv.visitMethodInsn(INVOKESTATIC, "cz/encircled/eprofiler/Profiler", "methodStart", "()Lcz/encircled/eprofiler/MethodState;", false);
        mv.visitVarInsn(ASTORE, 1);
    }

    @Override
    protected void onMethodExit(int opcode) {
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEINTERFACE, "cz/encircled/eprofiler/MethodState", "end", "()V", true);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 1, maxLocals);
    }

}
