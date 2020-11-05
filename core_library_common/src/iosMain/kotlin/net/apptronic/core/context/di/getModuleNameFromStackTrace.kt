package net.apptronic.core.context.di

actual fun generatedModuleName(): String? {
    return Throwable().getStackTrace().getOrNull(1)
}