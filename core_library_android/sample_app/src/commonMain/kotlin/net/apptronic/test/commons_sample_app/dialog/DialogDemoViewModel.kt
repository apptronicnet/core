package net.apptronic.test.commons_sample_app.dialog

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.common.textInput
import net.apptronic.core.mvvm.viewmodel.EMPTY_VIEW_MODEL_CONTEXT
import net.apptronic.core.mvvm.viewmodel.ViewModel

class DialogDemoViewModel(parent: Context) : ViewModel(parent, EMPTY_VIEW_MODEL_CONTEXT) {

    val message = textInput("Dialog message")

    val dialogNavigator = stackNavigator()

    fun onShowDialogClick() {
        dialogNavigator.add(MessageDialogViewModel(context, message.getText()))
    }

}