package net.apptronic.test.commons_sample_compat_app.fragments.enterdata.location

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.property
import net.apptronic.core.entity.function.and
import net.apptronic.core.entity.function.isNotEmpty
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.commons.textInput
import net.apptronic.core.viewmodel.commons.withOnUpdate
import net.apptronic.core.viewmodel.viewModelContext
import net.apptronic.test.commons_sample_compat_app.fragments.enterdata.DataRepository
import net.apptronic.test.commons_sample_compat_app.fragments.enterdata.EnterDataRouter

fun Contextual.enterLocationViewModel() = EnterLocationViewModel(viewModelContext())

class EnterLocationViewModel(context: ViewModelContext) : ViewModel(context) {

    private val repository = inject<DataRepository>()

    val country = textInput(repository.country)
        .withOnUpdate {
            repository.country = it
        }
    val city = textInput(repository.city)
        .withOnUpdate {
            repository.city = it
        }

    val isSubmitEnabled = property(country.isNotEmpty() and city.isNotEmpty())
    val onSubmitClick = genericEvent()

    init {
        doOnBind {
            val router = inject<EnterDataRouter>()
            onSubmitClick.subscribe {
                router.submitLocation()
            }
        }
    }

}