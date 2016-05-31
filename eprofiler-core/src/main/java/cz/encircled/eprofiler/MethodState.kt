package cz.encircled.eprofiler

/**
 * @author Kisel on 24.05.2016.
 */
interface MethodState {

    var id: Long

    var parent: MethodState?

    fun setStartTime(time: Long?)

    fun end()

    fun addParam(value: String)

    fun hasParent(): Boolean

    fun addNested(state: MethodState): MethodState

}
