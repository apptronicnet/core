package net.apptronic.test.commons_sample_compat_app.about

import net.apptronic.core.commons.timer.timer
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.functions.map
import net.apptronic.core.entity.operators.increment
import net.apptronic.core.entity.property
import net.apptronic.core.entity.value
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.viewModelContext

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