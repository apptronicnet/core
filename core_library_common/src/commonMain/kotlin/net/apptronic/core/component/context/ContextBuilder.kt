package net.apptronic.core.component.context

import net.apptronic.core.component.di.ModuleDefinition
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.component.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.component.lifecycle.LifecycleDefinition
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun Context.dependencyModule(moduleDefinition: ModuleDefinition) {
    dependencyDispatcher.addModule(moduleDefinition)
}

fun Context.dependencyModule(initializer: ModuleDefinition.() -> Unit) {
    dependencyDispatcher.addModule(declareModule("", initializer))
}

fun Contextual.childContext(lifecycleDefinition: LifecycleDefinition = BASE_LIFECYCLE, builder: Context.() -> Unit = {}): Context {
    val subContext = SubContext(parent = context, lifecycleDefinition = lifecycleDefinition)
    subContext.builder()
    return subContext
}

fun <T : Context> Contextual.childContext(contextDefinition: ContextDefinition<T>): Context {
    return contextDefinition.createContext(context)
}

fun Contextual.viewModelContext(definition: ContextDefinition<ViewModelContext>): ViewModelContext {
    return definition.createContext(context)
}

fun Contextual.viewModelContext(name: String = "ViewModelContext", builder: ViewModelContext.() -> Unit = {}): ViewModelContext {
    val viewModelContext = ViewModelContext(parent = context, name = name)
    viewModelContext.builder()
    return viewModelContext
}