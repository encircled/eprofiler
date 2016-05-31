package cz.encircled.eprofiler.asm

import cz.encircled.eprofiler.Util
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 * @author Kisel on 30.05.2016.
 */

class ClassAdapter(cv: ClassVisitor?, owner: String) : ClassVisitor(Util.version, cv) {

    override fun visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array<String>): MethodVisitor? {
        var mv: MethodVisitor? = cv.visitMethod(access, name, desc, signature, exceptions)
        if (mv != null) {
            mv = ProfilerMethodAdapter(mv, access, name, desc)
        }
        return mv
    }

}