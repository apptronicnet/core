package net.apptronic.test.commons_sample_app.stackloading

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.contextCoroutineScope
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.inject
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel

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