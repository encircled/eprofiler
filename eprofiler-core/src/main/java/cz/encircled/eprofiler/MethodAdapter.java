package cz.encircled.eprofiler;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author Vlad on 23-May-16.
 */
public class MethodAdapter extends AdviceAdapter {

    final String methodName;

    ThreadLocal<State> state = new ThreadLocal<State>() {
        @Override
        protected State initialValue() {
            return new State();
        }
    };


    protected MethodAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc);
        methodName = name;
    }

    @Override
    protected void onMethodEnter() {
        Agent.getWriter().info("Start " + methodName);
        mv.visitMethodInsn(INVOKESTATIC, "cz/encircled/eprofiler/ConsoleWriter",
                "info", "()J", false);

    }

    @Override
    protected void onMethodExit(int opcode) {
        Agent.getWriter().info("End " + methodName + ", takes " + (Timer.now - state.get().start));
    }

}
