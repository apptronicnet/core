package net.apptronic.test.commons_sample_app.stackloading

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.genericEvent
import net.apptronic.core.mvvm.viewmodel.EMPTY_VIEW_MODEL_CONTEXT
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.actualModel
import net.apptronic.core.mvvm.viewmodel.navigation.progress
import net.apptronic.core.mvvm.viewmodel.navigation.size
import net.apptronic.core.mvvm.viewmodel.navigation.visibleModel

class StackLoadingViewModel(parent: Context) : ViewModel(parent, EMPTY_VIEW_MODEL_CONTEXT),
    StackRouter {

    init {
        context.dependencyDispatcher().addInstance(StackRouterDescriptor, this)
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
    val isBackButtonEnabled = navigator.size().map { it > 1 }

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