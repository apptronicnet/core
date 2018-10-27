package net.apptronic.shoppinglist.uisample

import net.apptronic.common.android.ui.components.FragmentLifecycle
import net.apptronic.common.android.ui.components.FragmentViewModel
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

class SampleViewModel(lifecycleHolder: LifecycleHolder<FragmentLifecycle>) : FragmentViewModel(lifecycleHolder) {

    val title = property("Sample")

    val onClickRefreshTitle = userAction<Unit>()

    val textInput = userAction<String>()

    val userInput = property<String>()

    val onClickConfirmInput = userAction<Unit>()

    val userInputText = property<String>()

    val userConfirmedInputText = property<String>()

}