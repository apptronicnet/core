package net.apptronic.test.commons_sample_app.dialog

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.common.textInput
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.stackNavigator

class DialogDemoViewModel(parent: Context) : ViewModel(parent, EmptyViewModelContext) {

    val message = textInput("Dialog message")

    val dialogNavigator = stackNavigator()

    fun onShowDialogClick() {
        dialogNavigator.add(MessageDialogViewModel(context, message.getText()))
    }

}