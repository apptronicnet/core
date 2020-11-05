package net.apptronic.core.base.platform

const val IS_DEBUG = false

fun debugError(error: Error) {
    if (IS_DEBUG) {
        throw error
    } else {
        platformLogError(error)
    }
}