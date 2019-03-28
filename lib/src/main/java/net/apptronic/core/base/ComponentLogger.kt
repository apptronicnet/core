package net.apptronic.core.base

import net.apptronic.core.component.di.createDescriptor

val ComponentLoggerDescriptor = createDescriptor<ComponentLogger>()

class ComponentLogger {

    var logMethod: (String) -> Unit = {}

    fun log(text: String) {
        logMethod.invoke(text)
    }

}