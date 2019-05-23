package net.apptronic.core.base

import net.apptronic.core.component.di.createDescriptor

val ComponentLoggerDescriptor = createDescriptor<Logger>()

class Logger {

    fun setupLogging(logMethod: (String) -> Unit) {
        this.logMethod = logMethod
    }

    private var logMethod: (String) -> Unit = {}

    fun log(text: String) {
        logMethod.invoke(text)
    }

}