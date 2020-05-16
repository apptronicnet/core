package net.apptronic.test.commons_sample_app.dialog

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EMPTY_VIEW_MODEL_CONTEXT
import net.apptronic.core.mvvm.viewmodel.ViewModel

class MessageDialogViewModel(parent: Context, message: String) :
    ViewModel(parent, EMPTY_VIEW_MODEL_CONTEXT) {

    private val messageText = value(message)

    val text =
        messageText.map {
            if (it.isNotBlank()) it else "No message"
        }.map {
            "Message: $message"
        }

}