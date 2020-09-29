package net.apptronic.core

actual fun <T> T.neverFrozen(): T {
    return this
}