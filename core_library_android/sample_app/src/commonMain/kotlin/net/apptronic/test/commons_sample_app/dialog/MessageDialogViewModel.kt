package net.apptronic.test.commons_sample_app.dialog

import net.apptronic.core.context.Context
import net.apptronic.core.entity.functions.map
import net.apptronic.core.entity.value
import net.apptronic.core.viewmodel.EmptyViewModelContext
import net.apptronic.core.viewmodel.ViewModel

class MessageDialogViewModel(parent: Context, message: String) :
    ViewModel(parent, EmptyViewModelContext) {

    private val messageText = value(message)

    val text =
        messageText.map {
            if (it.isNotBlank()) it else "No message"
        }.map {
            "Message: $message"
        }

}