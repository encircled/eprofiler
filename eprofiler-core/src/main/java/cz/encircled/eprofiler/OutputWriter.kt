package cz.encircled.eprofiler

/**
 * @author Vlad on 23-May-16.
 */
interface OutputWriter {

    fun debug(message: String)

    fun warn(message: String)

    fun info(message: String)

}
