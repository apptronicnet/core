package net.apptronic.core.component.di

actual fun getModuleNameFromStackTrace(): String? {
    return Throwable().getStackTrace().getOrNull(4)
}