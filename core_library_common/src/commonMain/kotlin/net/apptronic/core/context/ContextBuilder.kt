package net.apptronic.core.context

import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.declareModule
import net.apptronic.core.context.di.generatedModuleName

fun Context.dependencyModule(moduleDefinition: ModuleDefinition) {
    dependencyDispatcher.addModule(moduleDefinition)
}

fun Context.dependencyModule(name: String? = generatedModuleName(), initializer: ModuleDefinition.() -> Unit) {
    dependencyDispatcher.addModule(declareModule(name, initializer))
}
