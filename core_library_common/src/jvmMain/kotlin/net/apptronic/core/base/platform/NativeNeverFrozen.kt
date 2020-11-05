package net.apptronic.core.base.platform

actual fun <T> T.neverFrozen(): T {
    return this
}