package net.apptronic.test.commons_sample_app.dialog

import net.apptronic.core.context.Context
import net.apptronic.core.viewmodel.EmptyViewModelContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.commons.textInput
import net.apptronic.core.viewmodel.navigation.stackNavigator

class DialogDemoViewModel(parent: Context) : ViewModel(parent, EmptyViewModelContext) {

    val message = textInput("Dialog message")

    val dialogNavigator = stackNavigator()

    fun onShowDialogClick() {
        dialogNavigator.add(MessageDialogViewModel(context, message.getText()))
    }

}