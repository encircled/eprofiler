package cz.encircled.eprofiler.asm

import cz.encircled.eprofiler.MethodState
import cz.encircled.eprofiler.Profiler
import cz.encircled.eprofiler.Util
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @author Vlad on 23-May-16.
 */
class ProfilerMethodAdapter constructor(mv: MethodVisitor, access: Int, name: String, desc: String) : AdviceAdapter(Util.version, mv, access, name, desc) {

    private var index: Int = 0

    override fun onMethodEnter() {
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(Profiler::class.java), "methodStart",
                Type.getMethodDescriptor(Util.getMethod(Profiler::class.java, "methodStart")), false)
        index = newLocal(Type.getType(MethodState::class.java))
        mv.visitVarInsn(Opcodes.ASTORE, index)

    }

    override fun onMethodExit(opcode: Int) {
        mv.visitVarInsn(Opcodes.ALOAD, index)
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, Type.getInternalName(MethodState::class.java), "end", Type.getMethodDescriptor(Util.getMethod(MethodState::class.java, "end")),
                true)
    }

}
