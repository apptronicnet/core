package net.apptronic.test.commons_sample_app.stackloading

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.ViewModel

fun Contextual.stackItemViewModel(name: String) =
    StackItemViewModel(childContext(), name)

class StackItemViewModel(context: Context, val name: String) : ViewModel(context) {

    private val router = inject(StackRouterDescriptor)

    val text = value<String>()

    val onClickAdd = genericEvent {
        router.navigatorAdd()
    }

    val onClickReplace = genericEvent {
        router.navigatorReplace()
    }

    val onClickReplaceAll = genericEvent {
        router.navigatorReplaceAll()
    }

    init {
        contextCoroutineScope.launch {
            delay(1000)
            text.set(name)
        }
    }

}