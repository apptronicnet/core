package net.apptronic.test.commons_sample_app.dialog

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.commons.textInput
import net.apptronic.core.viewmodel.navigation.stackNavigator

fun Contextual.dialogDemoViewModel() = DialogDemoViewModel(childContext())

class DialogDemoViewModel internal constructor(context: Context) : ViewModel(context) {

    val message = textInput("Dialog message")

    val dialogNavigator = stackNavigator()

    fun onShowDialogClick() {
        dialogNavigator.add(messageDialogViewModel(message.get()))
    }

}