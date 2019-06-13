package net.apptronic.core.base.concurrent

actual fun <T : Any> requireNeverFrozen(target: T): T {
    // not needed for JVM
    return target
}