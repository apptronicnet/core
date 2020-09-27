package net.apptronic.core.view.binder.target

interface TargetBridge<T> {

    fun assignTarget(target: T)

    fun releaseTarget()

}