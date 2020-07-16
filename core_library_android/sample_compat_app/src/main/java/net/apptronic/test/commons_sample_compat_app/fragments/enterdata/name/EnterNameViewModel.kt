package net.apptronic.test.commons_sample_compat_app.fragments.enterdata.name

import android.util.Log
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

fun Contextual.enterNameViewModel() = EnterNameViewModel(viewModelContext())

class EnterNameViewModel(context: ViewModelContext) : ViewModel(context) {

    private val repository = inject<DataRepository>()

    val firstName = textInput(repository.firstName)
        .withOnUpdate {
            repository.firstName = it
        }
    val lastName = textInput(repository.lastName)
        .withOnUpdate {
            repository.lastName = it
        }

    val isContinueEnabled = property(firstName.text().isNotEmpty() and lastName.text().isNotEmpty())
    val onContinueClick = genericEvent()

    private val routers = mutableListOf<EnterDataRouter>()

    init {
        doOnBind {
            val router = inject<EnterDataRouter>()
            routers.add(router)
            onContinueClick.subscribe {
                val list = routers
                router.submitName()
            }
            onExit {
                Log.i("EnterNameViewModel", "Exit")
            }
        }
    }

}