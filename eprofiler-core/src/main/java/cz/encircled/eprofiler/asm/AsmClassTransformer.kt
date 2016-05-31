package cz.encircled.eprofiler.asm

import cz.encircled.eprofiler.core.ProfilerCore
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter
import java.lang.instrument.ClassFileTransformer
import java.lang.instrument.IllegalClassFormatException
import java.security.ProtectionDomain
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author Vlad on 23-May-16.
 */
class AsmClassTransformer() : ClassFileTransformer {

    private val classMatchers: List<Matcher>

    init {
        classMatchers = ProfilerCore.configuration.getClassNamePatterns().map({ pattern -> Pattern.compile(pattern).matcher("") }).toList()
        ProfilerCore.outputWriter.debug("ASM transformer created")
    }

    @Throws(IllegalClassFormatException::class)
    override fun transform(loader: ClassLoader, className: String, classBeingRedefined: Class<*>, protectionDomain: ProtectionDomain, classfileBuffer: ByteArray): ByteArray {
        val dottedClassName = className.replace("/".toRegex(), "\\.")

        println(dottedClassName)
        if (isNotInternal(dottedClassName) && matchesPattern(dottedClassName)) {
            ProfilerCore.outputWriter.debug("Transform " + dottedClassName)

            val reader = ClassReader(classfileBuffer)
            val writer = ClassWriter(reader, ClassWriter.COMPUTE_FRAMES)

            val rootVisitor: ClassVisitor
            if (ProfilerCore.configuration.isShowBytecode) {
                val visitor = TraceClassVisitor(writer, PrintWriter(System.out))
                rootVisitor = ClassAdapter(visitor, className)
            } else {
                rootVisitor = ClassAdapter(writer, className)
            }

            reader.accept(rootVisitor, ClassReader.EXPAND_FRAMES)
            return writer.toByteArray()
        }

        return classfileBuffer
    }

    private fun isNotInternal(dottedClassName: String): Boolean {
        return !dottedClassName.startsWith("cz.encircled.eprofiler") || dottedClassName.startsWith("cz.encircled.eprofiler.test")
    }

    private fun matchesPattern(dottedClassName: String): Boolean {
        for (classMatcher in classMatchers) {
            if (classMatcher.reset(dottedClassName).matches()) {
                return true
            }
        }

        return false
    }

}
