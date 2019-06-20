package net.apptronic.test.commons_sample_app.loadfilterlist

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.behavior.delay
import net.apptronic.core.component.entity.behavior.whenTrue
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.anyValue
import net.apptronic.core.component.entity.functions.not
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.container.OnReadyForLoad

fun createLoadItemViewModel(parent: Context, time: Long, initialReady: Boolean): ViewModel {
    val context = ViewModelContext(parent)
    return LoadItemViewModel(context, time, initialReady)
}

class LoadItemViewModel(
    context: ViewModelContext, time: Long, initialReady: Boolean
) : ViewModel(context), OnReadyForLoad {

    private val readyToLoad = value(initialReady)

    val text = value<String>()

    val isLoading = value(true).setAs(text.anyValue().not())

    init {
        readyToLoad.whenTrue().delay(time).subscribe {
            text.set("This is loaded in $time ms")
        }
    }

    override fun setReadyForLoad() {
        readyToLoad.set(true)
    }

}