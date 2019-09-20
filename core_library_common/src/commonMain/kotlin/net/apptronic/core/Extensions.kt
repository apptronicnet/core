package net.apptronic.core

const val IS_DEBUG = true

fun debugError(error: Error) {
    if (IS_DEBUG) {
        throw error
    } else {
        platformLogError(error)
    }
}