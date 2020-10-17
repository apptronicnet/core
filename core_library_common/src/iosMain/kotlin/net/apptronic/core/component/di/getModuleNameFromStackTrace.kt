package net.apptronic.core.component.di

actual fun generatedModuleName(): String? {
    return Throwable().getStackTrace().getOrNull(1)
}