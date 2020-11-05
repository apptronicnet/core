package net.apptronic.test.commons_sample_app.staknavigation

import net.apptronic.core.context.Context
import net.apptronic.core.context.ContextDefinition
import net.apptronic.core.entity.functions.map
import net.apptronic.core.entity.genericEvent
import net.apptronic.core.entity.property
import net.apptronic.core.entity.value
import net.apptronic.core.viewmodel.EmptyViewModelContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.navigation.BackNavigationStatus
import net.apptronic.core.viewmodel.navigation.HasBackNavigation

class StackItemViewModel(
    parent: Context, contextDefinition: ContextDefinition<ViewModelContext> = EmptyViewModelContext,
    private val index: Int, backgroundColor: StackItemColor
) : ViewModel(parent, contextDefinition), HasBackNavigation {

    private val indexProperty = property(index)

    val text = property(indexProperty.map { "Page #$it" })
    val backgroundColor = property(backgroundColor)

    val onGoBack = genericEvent()
    val onCantGoBack = genericEvent()

    val isBackNavigationAllowed = value(true)

    override fun toString(): String {
        return "StackItemViewModel#${index}"
    }

    override fun getBackNavigationStatus(): BackNavigationStatus {
        return if (isBackNavigationAllowed.get()) {
            BackNavigationStatus.Allowed
        } else {
            BackNavigationStatus.Restricted
        }
    }

    override fun onBackNavigationRestrictedEvent() {
        super.onBackNavigationRestrictedEvent()
        onCantGoBack.sendEvent()
    }

    override fun onBackNavigationConfirmedEvent() {
        super.onBackNavigationConfirmedEvent()
    }

}