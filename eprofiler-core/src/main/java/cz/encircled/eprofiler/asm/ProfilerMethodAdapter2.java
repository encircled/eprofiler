package cz.encircled.eprofiler.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Vlad on 24-May-16.
 */
public class ProfilerMethodAdapter2 extends MethodVisitor {

    private final Label start = new Label(),
            end = new Label(),
            handler = new Label();

    public ProfilerMethodAdapter2(int i, MethodVisitor methodVisitor) {
        super(i, methodVisitor);
    }


    @Override
    public void visitCode() {
        mv.visitCode();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cz/encircled/eprofiler/Profiler", "methodStart", "()Lcz/encircled/eprofiler/MethodState;", false);
        mv.visitVarInsn(Opcodes.ASTORE, 1);
        visitTryCatchBlock(start,
                end,
                handler,
                "java/lang/Exception");
        visitLabel(start);
        super.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
        visitJumpInsn(Opcodes.GOTO, end);
        visitLabel(handler);

        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "cz/encircled/eprofiler/MethodState", "end", "()V", true);
        super.visitInsn(opcode);
    }

}
