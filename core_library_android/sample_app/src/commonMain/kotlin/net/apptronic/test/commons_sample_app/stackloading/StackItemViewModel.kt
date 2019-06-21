package net.apptronic.test.commons_sample_app.stackloading

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.delay
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.anyValue
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelWithVisibility

class StackItemViewModel(context: ViewModelContext, val name: String) : ViewModel(context),
    ViewModelWithVisibility {

    private val router = getProvider().inject(StackRouterDescriptor)

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