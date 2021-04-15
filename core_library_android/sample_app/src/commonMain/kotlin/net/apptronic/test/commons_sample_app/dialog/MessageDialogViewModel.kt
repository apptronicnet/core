package net.apptronic.test.commons_sample_app.dialog

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.map
import net.apptronic.core.viewmodel.ViewModel

fun Contextual.messageDialogViewModel(message: String) =
    MessageDialogViewModel(childContext(), message)

class MessageDialogViewModel internal constructor(context: Context, message: String) :
    ViewModel(context) {

    private val messageText = value(message)

    val text =
        messageText.map {
            if (it.isNotBlank()) it else "No message"
        }.map {
            "Message: $message"
        }

}