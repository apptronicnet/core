package net.apptronic.test.commons_sample_compat_app.about

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.updaters.increment
import net.apptronic.core.component.property
import net.apptronic.core.component.timer
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun Contextual.aboutViewModel() = AboutViewModel(viewModelContext())

class AboutViewModel(context: ViewModelContext) : ViewModel(context) {

    private val tick = value(0)
    private val timer = timer(1000L) {
        tick.increment()
    }

    val timerText = property(tick.map { "Seconds online: $it" })

    init {
        doOnFocused {
            timer.start()
        }
    }

}