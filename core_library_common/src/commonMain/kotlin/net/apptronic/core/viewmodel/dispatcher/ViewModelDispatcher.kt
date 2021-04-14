package net.apptronic.core.viewmodel.dispatcher

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.context.terminate
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelParent
import kotlin.reflect.KClass

inline fun <reified T : IViewModel> IComponent.viewModelDispatcher(noinline builder: (Contextual) -> T): ViewModelDispatcher<T> {
    return viewModelDispatcher(T::class, builder)
}

fun <T : IViewModel> IComponent.viewModelDispatcher(
    type: KClass<T>,
    builder: (Contextual) -> T
): ViewModelDispatcher<T> {
    val dispatcher = ViewModelDispatcher(context, type, builder)
    context.dependencyProvider.optional<ViewDispatcher>()?.onNextViewModelDispatcher(dispatcher)
    return dispatcher;
}

/**
 * Class which managing root [ViewModel] state and provides it to [ViewContainer]
 */
class ViewModelDispatcher<T : IViewModel> internal constructor(
    context: Context,
    private val type: KClass<T>,
    private val builder: (Contextual) -> T
) : Component(context), ViewModelParent {

    private var viewModel: T? = null
    private var viewContainer: ViewContainer<T>? = null

    /**
     * Get type of view model for this dispatcher
     */
    val viewModelType: KClass<T>
        get() = type

    /**
     * Check is [ViewModelDispatcher] have active view model or not
     */
    fun haveActiveViewModel(): Boolean {
        return viewModel != null
    }

    /**
     * Get currently active [ViewModel]. If currently no [ViewModel] active - it will be automatically created.
     */
    fun obtainViewModel(): T {
        return viewModel ?: builder(context).also {
            it.onAttachToParent(this)
            viewModel = it
        }
    }

    /**
     * Register [ViewContainer] which requires to bind to [ViewModel]. Uses currently active [ViewModel] or creates it
     * if it is not present.
     */
    fun registerContainer(container: ViewContainer<T>) {
        if (viewContainer != null) {
            throw IllegalStateException("UiContainer already set")
        }
        this.viewContainer = container
        val viewModel = obtainViewModel()
        container.onAddedViewModel(viewModel)
    }

    /**
     * Releases [ViewContainer], meaning there is no UI which can be bound to active [ViewModel]. This is not destroying
     * [ViewModel]. To terminate [ViewModel] it needed to explicitly call [recycleViewModel]
     */
    fun unregisterContainer(container: ViewContainer<T>) {
        if (viewContainer == null) {
            throw IllegalStateException("UiContainer was not set")
        }
        if (viewContainer != container) {
            throw IllegalStateException("Should unregister same UiContainer")
        }
        viewContainer = null
    }

    /**
     * Recycles active [ViewModel] if it is present.
     */
    fun recycleViewModel() {
        viewModel?.onDetachFromParent()
        viewModel?.terminate()
        viewModel = null
    }

    override fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any?) {
        viewContainer?.onViewModelRequestedCloseSelf()
    }

}