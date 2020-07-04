package net.apptronic.test.commons_sample_app.staknavigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.property
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class StackItemViewModel(
    parent: Context, contextDefinition: ContextDefinition<ViewModelContext> = EmptyViewModelContext,
    private val index: Int, backgroundColor: StackItemColor
) : ViewModel(parent, contextDefinition) {

    private val indexProperty = property(index)

    val text = property(indexProperty.map { "Page #$it" })
    val backgroundColor = property(backgroundColor)

    override fun toString(): String {
        return "StackItemViewModel#${index}"
    }

}