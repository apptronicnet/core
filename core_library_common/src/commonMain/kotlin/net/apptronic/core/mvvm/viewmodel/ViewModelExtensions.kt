package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context

fun <T : Component> T?.setupView(block: T.() -> Unit) {
    this?.apply(block)
}

inline fun <reified T : ViewModel> Context.newViewModel(constructor: (ViewModelContext) -> T): T {
    val resultName = T::class.qualifiedName ?: T::class.simpleName ?: "Unknown"
    val viewModelContext = ViewModelContext(this, resultName)
    return constructor.invoke(viewModelContext)
}