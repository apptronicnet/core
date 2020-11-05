package net.apptronic.test.commons_sample_app.loadfilterlist

import net.apptronic.core.context.Context
import net.apptronic.core.entity.behavior.delay
import net.apptronic.core.entity.behavior.whenTrue
import net.apptronic.core.entity.commons.setAs
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.anyValue
import net.apptronic.core.entity.function.not
import net.apptronic.core.viewmodel.EmptyViewModelContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.OnReadyForLoad

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