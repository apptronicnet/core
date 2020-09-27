package net.apptronic.core.view.binder

/**
 * This class bridges method to [Target] to be invoked
 */
internal class ParamInvocationBridge<Target, Param, E>(
        private val invocation: Target.(Param) -> E,
) : TargetBridge<Target> {

    private var target: Target? = null

    override fun assignTarget(target: Target) {
        this.target = target
    }

    override fun releaseTarget() {
        this.target = null
    }

    fun invoke(parameter: Param): E {
        return target!!.invocation(parameter)
    }

}