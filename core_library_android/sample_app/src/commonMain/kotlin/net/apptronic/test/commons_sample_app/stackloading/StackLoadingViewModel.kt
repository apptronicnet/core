package net.apptronic.test.commons_sample_app.stackloading

import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.actualModel
import net.apptronic.core.mvvm.viewmodel.navigation.progress
import net.apptronic.core.mvvm.viewmodel.navigation.visibleModel

class StackLoadingViewModel(context: ViewModelContext) : ViewModel(context), StackRouter {

    init {
        getProvider().addInstance(StackRouterDescriptor, this)
    }

    private var index = 1

    val navigator = stackNavigator()
    val isInProgress = navigator.progress()

    val loadingIndicatorText = isInProgress.map { if (it) "Loading" else "-" }
    val actualIndicatorText =
        navigator.actualModel().map { "Actual: " + (it as? StackItemViewModel)?.name }
    val visibleIndicatorText =
        navigator.visibleModel().map { "Visible: " + (it as? StackItemViewModel)?.name }

    val onClickBack = genericEvent {
        navigator.popBackStack(BasicTransition.Back)
    }
    val isBackButtonEnabled = value<Boolean>()

    private fun newItem(name: String): ViewModel {
        return StackItemViewModel(ViewModelContext(this), "$name #${index++}")
    }

    init {
        navigator.setSimpleVisibilityFilter()
        navigator.add(newItem("Root"), BasicTransition.Fade)
        navigator.subscribe {
            isBackButtonEnabled.set(navigator.getSize() > 1)
        }
    }

    override fun navigatorAdd() {
        navigator.add(newItem("Added"), BasicTransition.Forward)
    }

    override fun navigatorReplace() {
        navigator.replace(newItem("Replaced"), BasicTransition.Fade)
    }

    override fun navigatorReplaceAll() {
        navigator.replaceAll(newItem("Replaced all"), BasicTransition.Fade)
    }

}