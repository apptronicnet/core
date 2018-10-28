package net.apptronic.shoppinglist.uisample

import androidx.core.content.ContextCompat
import net.apptronic.common.android.ui.components.FragmentLifecycle
import net.apptronic.common.android.ui.components.FragmentViewModel
import net.apptronic.common.android.ui.components.submodels.TextViewModel
import net.apptronic.common.android.ui.viewmodel.entity.asFunctionFrom
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.shoppinglist.R

/**
 * Represents sample screen properties, state and events
 */
class SampleViewModel(lifecycleHolder: LifecycleHolder<FragmentLifecycle>) : FragmentViewModel(lifecycleHolder) {

    /**
     * Title text
     */
    val title = value("Sample")

    /**
     * Action when user clicked button "Refresh title"
     */
    val onClickRefreshTitle = genericEvent()

    /**
     * Action when user changed text in EditText input
     */
    val userInputUpdates = typedEvent<String>()

    /**
     * Property where saved changes from EditText input
     */
    val userInputValue = value<String>()

    /**
     * Action when user clicked button "Confirm text input"
     */
    val onClickConfirmInputEvent = genericEvent()

    /**
     * User input text
     */
    val currentInputText = value<String>()

    val confirmedInputText = TextViewModel(this).apply {
        textColor.asFunctionFrom(context, text, userInputValue) { context, text, userInputValue ->
            if (text == userInputValue) {
                ContextCompat.getColor(context, R.color.blueText)
            } else {
                ContextCompat.getColor(context, R.color.redText)
            }
        }
    }

}

