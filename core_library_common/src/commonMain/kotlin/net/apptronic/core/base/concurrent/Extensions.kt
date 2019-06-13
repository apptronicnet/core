package net.apptronic.core.base.concurrent

fun <T : Any> T.requireNeverFrozen() {
    requireNeverFrozen(this)
}

expect fun <T : Any> requireNeverFrozen(target: T): T