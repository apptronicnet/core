package net.apptronic.core

actual fun platformLogError(error: Error) {
    error.printStackTrace(System.err)
}