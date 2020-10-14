package net.apptronic.test.commons_sample_app.stackloading

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.genericEvent
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.*

fun Contextual.stackLoadingViewModel() = StackLoadingViewModel(viewModelContext())

class StackLoadingViewModel internal constructor(context: ViewModelContext) : ViewModel(context),
    StackRouter {

    init {
        context.dependencyDispatcher.addInstance(StackRouterDescriptor, this)
    }

    private var index = 1

    val navigator = stackNavigator()
    val isInProgress = navigator.content.progress()

    val loadingIndicatorText = isInProgress.map { if (it) "Loading" else "-" }
    val actualIndicatorText =
        navigator.content.actualModel().map { "Actual: " + (it as? StackItemViewModel)?.name }
    val visibleIndicatorText =
        navigator.content.visibleModel().map { "Visible: " + (it as? StackItemViewModel)?.name }

    val onClickBack = genericEvent {
        navigator.popBackStack(BasicTransition.Backward)
    }
    val isBackButtonEnabled = navigator.content.size().map { it > 1 }

    private fun newItem(name: String): ViewModel {
        return StackItemViewModel(context, "$name #${index++}")
    }

    init {
        navigator.setSimpleVisibilityFilter()
        navigator.add(newItem("Root"), BasicTransition.Fade)
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