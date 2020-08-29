package net.apptronic.core

import kotlin.native.concurrent.ensureNeverFrozen

actual fun <T> T.neverFrozen(): T {
    this!!.ensureNeverFrozen()
    return this
}