package net.apptronic.core.view.binder

interface TargetBridge<T> {

    fun assignTarget(target: T)

    fun releaseTarget()

}