package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition

fun <T : Component> T?.setupView(block: T.() -> Unit) {
    this?.apply(block)
}

inline fun <reified T : ViewModel> Context.createViewModel(constructor: (ContextDefinition<ViewModelContext>) -> T): T {
    val resultName = T::class.qualifiedName ?: T::class.simpleName ?: "Unknown"
    val viewModelContext = defineViewModelContext(resultName)
    return constructor.invoke(viewModelContext)
}