package net.apptronic.core.context

import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.declareModule
import net.apptronic.core.context.di.generatedModuleName
import net.apptronic.core.context.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.context.lifecycle.LifecycleDefinition

fun Context.dependencyModule(moduleDefinition: ModuleDefinition) {
    dependencyDispatcher.addModule(moduleDefinition)
}

fun Context.dependencyModule(name: String? = generatedModuleName(), initializer: ModuleDefinition.() -> Unit) {
    dependencyDispatcher.addModule(declareModule(name, initializer))
}

fun Contextual.childContext(lifecycleDefinition: LifecycleDefinition = BASE_LIFECYCLE, builder: Context.() -> Unit = {}): Context {
    val subContext = SubContext(parent = context, lifecycleDefinition = lifecycleDefinition)
    subContext.builder()
    return subContext
}

fun <T : Context> Contextual.childContext(contextDefinition: ContextDefinition<T>): T {
    return contextDefinition.createContext(context)
}
