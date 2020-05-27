package net.apptronic.test.commons_sample_app.stackloading

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.delay
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.anyValue
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.inject
import net.apptronic.core.component.newChain
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelWithVisibility

class StackItemViewModel(parent: Context, val name: String) :
    ViewModel(parent, EmptyViewModelContext),
    ViewModelWithVisibility {

    private val router = inject(StackRouterDescriptor)

    override fun isReadyToShow(): Entity<Boolean> {
        return isReadyToShow
    }

    val text = value<String>()
    private val isReadyToShow = value(false)
        .setAs(text.anyValue())

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
        newChain().delay(1000).subscribe {
            text.set(name)
        }
    }

}