package net.apptronic.test.commons_sample_app.staknavigation

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.property
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.map
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.BackNavigationStatus
import net.apptronic.core.viewmodel.navigation.HasBackNavigation

fun Contextual.stackItemViewModel(index: Int, backgroundColor: StackItemColor) =
    StackItemViewModel(childContext(), index, backgroundColor)

class StackItemViewModel(
    context: Context, private val index: Int, backgroundColor: StackItemColor
) : ViewModel(context), HasBackNavigation {

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
        onCantGoBack.update()
    }

    override fun onBackNavigationConfirmedEvent() {
        super.onBackNavigationConfirmedEvent()
    }

}