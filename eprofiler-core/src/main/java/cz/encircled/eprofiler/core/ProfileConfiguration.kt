package cz.encircled.eprofiler.core

import cz.encircled.eprofiler.Util
import java.util.*

/**
 * @author Vlad on 23-May-16.
 */
open class ProfileConfiguration(args: String?) {

    private val classNamePatterns = ArrayList<String>(2)

    var isDebug: Boolean = false
        private set

    var isShowBytecode: Boolean = false
        private set

    var minDurationToLog = 10L
        private set

    init {
        init(args)
        validate()
    }

    private fun init(args: String?) {
        if (args != null) {
            for (fullParam in args.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                val keyAndVal = fullParam.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                when (keyAndVal.size) {
                    1 -> applyParameter(keyAndVal[0], null)
                    2 -> applyParameter(keyAndVal[0], keyAndVal[1])
                    else -> throw RuntimeException("Invalid argument " + fullParam)
                }
            }
        }
    }

    private fun applyParameter(key: String, value: String?) {
        when (key) {
            "classPattern" -> if (Util.isNotEmpty(value)) {
                classNamePatterns.add(value!!)
            }
            "debug" -> isDebug = true
            "showBytecode" -> isShowBytecode = true
            "minDurationToLog" -> if (Util.isNotEmpty(value)) {
                minDurationToLog = java.lang.Long.parseLong(value!!)
            }
        }
    }

    fun getClassNamePatterns(): List<String> {
        return classNamePatterns
    }

    private fun validate() {
        if (classNamePatterns.isEmpty()) {
            throw RuntimeException("Class name pattern must be not null")
        }
    }

}
