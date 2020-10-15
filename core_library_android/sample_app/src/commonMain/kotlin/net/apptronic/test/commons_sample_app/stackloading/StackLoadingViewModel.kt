package net.apptronic.test.commons_sample_app.stackloading

import kotlinx.coroutines.launch
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.coroutines.contextCoroutineScope
import net.apptronic.core.component.entity.conditions.awaitUntilSet
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.models.size
import net.apptronic.core.mvvm.viewmodel.navigation.models.viewModel
import net.apptronic.core.mvvm.viewmodel.navigation.stackNavigator

fun Contextual.stackLoadingViewModel() = StackLoadingViewModel(viewModelContext())

class StackLoadingViewModel internal constructor(context: ViewModelContext) : ViewModel(context),
    StackRouter {

    init {
        context.dependencyDispatcher.addInstance(StackRouterDescriptor, this)
    }

    private var index = 1

    val navigator = stackNavigator()
    val isInProgress = value(false)

    val loadingIndicatorText = isInProgress.map { if (it) "Loading" else "-" }
    val actualIndicatorText =
        navigator.content.viewModel().map { "Actual: " + (it as? StackItemViewModel)?.name }
    val visibleIndicatorText =
        navigator.content.viewModel().map { "Visible: " + (it as? StackItemViewModel)?.name }

    val onClickBack = genericEvent {
        navigator.popBackStack(BasicTransition.Backward)
    }
    val isBackButtonEnabled = navigator.content.size().map { it > 1 }

    private suspend fun newItem(name: String): ViewModel {
        isInProgress.set(true)
        val item = StackItemViewModel(context, "$name #${index++}")
        item.text.awaitUntilSet()
        isInProgress.set(false)
        return item
    }

    init {
        contextCoroutineScope.launch {
            navigator.add(newItem("Root"), BasicTransition.Fade)
        }
    }

    override fun navigatorAdd() {
        contextCoroutineScope.launch {
            navigator.add(newItem("Added"), BasicTransition.Forward)
        }
    }

    override fun navigatorReplace() {
        contextCoroutineScope.launch {
            navigator.replace(newItem("Replaced"), BasicTransition.Fade)
        }
    }

    override fun navigatorReplaceAll() {
        contextCoroutineScope.launch {
            navigator.replaceAll(newItem("Replaced all"), BasicTransition.Fade)
        }
    }

}