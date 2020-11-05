package net.apptronic.core.viewmodel

import net.apptronic.core.context.Context
import net.apptronic.core.context.ContextDefinition
import net.apptronic.core.context.component.IComponent

fun <T : IComponent> T?.setupView(block: T.() -> Unit) {
    this?.apply(block)
}

inline fun <reified T : IViewModel> Context.createViewModel(constructor: (ContextDefinition<ViewModelContext>) -> T): T {
    val resultName = T::class.qualifiedName ?: T::class.simpleName ?: "Unknown"
    val viewModelContext = defineViewModelContext(resultName)
    return constructor.invoke(viewModelContext)
}