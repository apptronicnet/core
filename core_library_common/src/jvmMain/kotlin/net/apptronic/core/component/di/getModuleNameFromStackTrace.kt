package net.apptronic.core.component.di

import net.apptronic.core.platformLogError

actual fun generatedModuleName(): String? {
    return try {
        Thread.currentThread().stackTrace.firstOrNull {
            !it.className.startsWith("java.lang.Thread")
                    && !it.className.startsWith("net.apptronic.core.component.di.GetModuleNameFromStackTraceKt")
                    && !it.className.startsWith("net.apptronic.core.component.di.ModuleKt")
                    && !it.className.startsWith("net.apptronic.core.component.context.ContextBuilderKt")
        }?.let {
            (it.fileName ?: it.className) + ":" + it.lineNumber
        }
    } catch (e: Exception) {
        platformLogError(Error(e))
        null
    }
}