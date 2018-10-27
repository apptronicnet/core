package net.apptronic.shoppinglist.uisample

import net.apptronic.common.android.ui.components.FragmentLifecycle
import net.apptronic.common.android.ui.components.FragmentViewModel
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

/**
 * Represents sample screen properties, state and events
 */
class SampleViewModel(lifecycleHolder: LifecycleHolder<FragmentLifecycle>) : FragmentViewModel(lifecycleHolder) {

    /**
     * Title text
     */
    val title = property("Sample")

    /**
     * Action when user clicked button "Refresh title"
     */
    val onClickRefreshTitle = userAction<Unit>()

    /**
     * Action when user changed text in EditText input
     */
    val textInput = userAction<String>()

    /**
     * Property where saved changes from EditText input
     */
    val userInput = state<String>()

    /**
     * Action when user clicked button "Confirm text input"
     */
    val onClickConfirmInput = userAction<Unit>()

    /**
     * User input text
     */
    val userInputText = property<String>()

    /**
     * Confirmed user input text
     */
    val userConfirmedInputText = property<String>()

}