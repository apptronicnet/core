package net.apptronic.test.commons_sample_app.stackloading

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.EmptyViewModelContext
import net.apptronic.core.viewmodel.ViewModel

class StackItemViewModel(parent: Context, val name: String) :
    ViewModel(parent, EmptyViewModelContext) {

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