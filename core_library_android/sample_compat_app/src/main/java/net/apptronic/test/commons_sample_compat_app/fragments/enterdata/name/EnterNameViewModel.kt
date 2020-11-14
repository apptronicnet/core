package net.apptronic.test.commons_sample_compat_app.fragments.enterdata.name

import android.util.Log
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

    val isContinueEnabled = property(firstName.isNotEmpty() and lastName.isNotEmpty())
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