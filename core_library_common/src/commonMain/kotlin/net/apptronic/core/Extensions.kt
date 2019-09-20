package net.apptronic.core

const val IS_DEBUG = false

fun debugError(error: Error) {
    if (IS_DEBUG) {
        throw error
    } else {
        platformLogError(error)
    }
}