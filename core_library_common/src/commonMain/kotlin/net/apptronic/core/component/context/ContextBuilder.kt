package net.apptronic.core.component.context

import net.apptronic.core.component.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.component.lifecycle.LifecycleDefinition
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun Contextual.childContext(lifecycleDefinition: LifecycleDefinition = BASE_LIFECYCLE, builder: Context.() -> Unit = {}): Context {
    val subContext = SubContext(parent = context, lifecycleDefinition = lifecycleDefinition)
    subContext.builder()
    return subContext
}

fun Contextual.viewModelContext(name: String = "ViewModelContext", builder: ViewModelContext.() -> Unit = {}): ViewModelContext {
    val viewModelContext = ViewModelContext(parent = context, name = name)
    viewModelContext.builder()
    return viewModelContext
}