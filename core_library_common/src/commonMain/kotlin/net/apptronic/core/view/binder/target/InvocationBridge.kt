package net.apptronic.core.view.binder.target

/**
 * This class bridges method to [Target] to be invoked
 */
class InvocationBridge<Target, E>(
        private val invocation: Target.() -> E,
) : TargetBridge<Target> {

    private var target: Target? = null

    override fun assignTarget(target: Target) {
        this.target = target
    }

    override fun releaseTarget() {
        this.target = null
    }

    fun invoke(): E {
        return target!!.invocation()
    }

}