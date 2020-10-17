package net.apptronic.core.component.di

actual fun getModuleNameFromStackTrace(): String? {
    return Thread.currentThread().stackTrace.toList()
            .getOrNull(4)?.let {
                (it.fileName ?: it.className) + ":" + it.lineNumber
            }
}