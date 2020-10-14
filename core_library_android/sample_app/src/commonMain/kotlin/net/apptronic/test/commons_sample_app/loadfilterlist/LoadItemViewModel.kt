package net.apptronic.test.commons_sample_app.loadfilterlist

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.behavior.delay
import net.apptronic.core.component.entity.behavior.whenTrue
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.anyValue
import net.apptronic.core.component.entity.functions.not
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.OnReadyForLoad

class LoadItemViewModel(
    parent: Context, index: Int, time: Long, initialReady: Boolean
) : ViewModel(parent, EmptyViewModelContext), OnReadyForLoad {

    private val readyToLoad = value(initialReady)

    val text = value<String>()

    val isLoading = value(true).setAs(text.anyValue().not())

    init {
        readyToLoad.whenTrue().delay(time).subscribe {
            text.set("[$index] This is loaded in $time ms")
        }
    }

    override fun setReadyForLoad() {
        readyToLoad.set(true)
    }

}