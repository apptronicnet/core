package net.apptronic.core.base.platform

actual fun platformLogError(error: Error) {
    error.printStackTrace(System.err)
}