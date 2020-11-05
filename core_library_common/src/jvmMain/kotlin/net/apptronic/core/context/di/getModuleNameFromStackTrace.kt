package net.apptronic.core.context.di

import net.apptronic.core.base.platform.platformLogError

actual fun generatedModuleName(): String? {
    return try {
        Thread.currentThread().stackTrace.firstOrNull {
            !it.className.startsWith("java.lang.Thread")
                    && !it.className.startsWith("net.apptronic.core.context.di.GetModuleNameFromStackTraceKt")
                    && !it.className.startsWith("net.apptronic.core.context.di.ModuleKt")
                    && !it.className.startsWith("net.apptronic.core.context.component.context.ContextBuilderKt")
        }?.let {
            (it.fileName ?: it.className) + ":" + it.lineNumber
        }
    } catch (e: Exception) {
        platformLogError(Error(e))
        null
    }
}