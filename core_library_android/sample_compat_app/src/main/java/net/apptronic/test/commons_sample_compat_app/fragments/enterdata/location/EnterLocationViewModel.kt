package net.apptronic.test.commons_sample_compat_app.fragments.enterdata.location

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.entity.entities.subscribe
import net.apptronic.core.component.entity.functions.and
import net.apptronic.core.component.entity.functions.isNotEmpty
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.inject
import net.apptronic.core.component.property
import net.apptronic.core.mvvm.common.textInput
import net.apptronic.core.mvvm.common.withOnUpdate
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
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

    val isSubmitEnabled = property(country.text().isNotEmpty() and city.text().isNotEmpty())
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