package net.apptronic.core.base.concurrent

import kotlin.native.concurrent.ensureNeverFrozen

actual fun <T : Any> requireNeverFrozen(target: T): T {
    target.ensureNeverFrozen()
    return target
}