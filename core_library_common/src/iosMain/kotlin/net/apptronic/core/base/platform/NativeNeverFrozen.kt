package net.apptronic.core.base.platform

import kotlin.native.concurrent.ensureNeverFrozen

actual fun <T> T.neverFrozen(): T {
    this!!.ensureNeverFrozen()
    return this
}