package net.apptronic.core.android.viewmodel

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import net.apptronic.core.android.viewmodel.view.ViewAdapter
import net.apptronic.core.base.platform.debugError
import net.apptronic.core.context.plugin.extensionDescriptor
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelLifecycle
import net.apptronic.core.viewmodel.navigation.ViewModelItem

private class SavedInstanceState(
    val hierarchyState: SparseArray<Parcelable>,
    val customState: Bundle
)

private val SavedInstanceStateExtensionDescriptor = extensionDescriptor<SavedInstanceState>()
private val ViewBinderExtensionsDescriptor = extensionDescriptor<ViewBinder<*>>()

fun IViewModel.requireBoundView() {
    val viewModel = this
    doOnVisible {
        if (extensions[ViewBinderExtensionsDescriptor] == null) {
            debugError(Error("${viewModel} have no ViewBinder"))
        }
    }
}

/**
 * Responsible for creating [View] and binding it to [ViewModel]. Generally implements "View" layer.
 *
 * There are several methods for usage with [Activity], [Dialog] or generic usage as [View] under
 * some [ViewGroup] container.
 */
abstract class ViewBinder<T : IViewModel> : ViewAdapter, BindingContainer {

    /**
     * Specify layout resource to be used for creation or [View] using default implementation.
     * If null - should override each method [onCreateView] to be used for corresponding binding
     * or navigator.
     */
    @LayoutRes
    open var layoutResId: Int? = null

    private var viewModelItemReference: ViewModelItem? = null
    private var viewModelReference: T? = null
    private var viewReference: View? = null
    private var bindingsReference: Bindings? = null
    private var savedInstanceStateReference: Bundle? = null
    private var outStateReference: Bundle? = null

    fun <E> viewBinding(builder: (View) -> E): ViewBindingProperty<E> {
        return ViewBindingProperty(this, builder)
    }

    fun <E> getBinding(builder: (View) -> E): E {
        return view.getBinding(builder)
    }

    fun <E> withBinging(builder: (View) -> E, action: E.() -> Unit) {
        with(view.getBinding(builder), action)
    }

    val viewModelItem: ViewModelItem
        get() {
            return viewModelItemReference
                ?: throw IllegalStateException("No ViewModelItem for $this")
        }

    val viewModel: T
        get() {
            return viewModelReference
                ?: throw IllegalStateException("No viewModel bound for $this")
        }

    val view: View
        get() {
            return viewReference
                ?: throw IllegalStateException("No view bound for $this")
        }

    val context: Context
        get() {
            return view.context
        }

    val savedInstanceState: Bundle?
        get() {
            return savedInstanceStateReference
        }

    val outState: Bundle
        get() {
            return outStateReference
                ?: throw IllegalStateException("Out state is not ready for $this")
        }

    private val bindings: Bindings
        get() {
            return bindingsReference ?: throw IllegalStateException("No bindings for $this")
        }

    private fun preCheck(viewModel: IViewModel) {
        if (viewModel.extensions[ViewBinderExtensionsDescriptor] != null) {
            debugError(Error("$viewModel already have bound view!!!"))
        }
        if (!viewModel.isStateBound()) {
            debugError(
                Error(
                    "$viewModel in stage ${
                        viewModel.context.lifecycle.getActiveStage()
                            ?.getStageName()
                    }"
                )
            )
        }

    }

    fun performViewBinding(viewModel: IViewModel, view: View) {
        preCheck(viewModel)
        performViewBindingInternal(viewModel, view)
    }

    /**
     * Bind [view] to [viewModelReference]
     */
    @Suppress("UNCHECKED_CAST")
    fun performViewBinding(item: ViewModelItem, view: View) {
        preCheck(item.viewModel)
        this.viewModelItemReference = item
        performViewBindingInternal(item.viewModel, view)

    }

    private fun performViewBindingInternal(viewModel: IViewModel, view: View) {
        this.viewReference = view
        this.viewModelReference = viewModel as T
        bindingsReference = Bindings(viewModel, this)

        val savedState = viewModel.extensions[SavedInstanceStateExtensionDescriptor]
        viewModel.extensions.remove(SavedInstanceStateExtensionDescriptor)
        savedState?.let {
            this.savedInstanceStateReference = it.customState
        }
        onBindView()
        savedState?.let {
            view.restoreHierarchyState(it.hierarchyState)
        }
        onViewStateRestored()
        outStateReference = Bundle()
        viewModel.extensions[ViewBinderExtensionsDescriptor] = this
        viewModel.doOnUnbind {
            val hierarchyState = SparseArray<Parcelable>()
            view.saveHierarchyState(hierarchyState)
            viewModel.extensions[SavedInstanceStateExtensionDescriptor] = SavedInstanceState(
                hierarchyState,
                outStateReference ?: Bundle()
            )
            bindings.unbind()
            bindingsReference = null
            viewModel.extensions.remove(ViewBinderExtensionsDescriptor)
        }
    }

    /**
     * Called when [view] is binding to the [viewModel]. At this time [viewModel] lifecycle
     * is in stage [ViewModelLifecycle.STAGE_BOUND]
     */
    protected abstract fun onBindView()

    protected open fun onViewStateRestored() {
        // implement by subclasses if needed
    }

    final override fun onUnbind(action: () -> Unit) {
        bindings.onUnbind(action)
    }

    final override fun add(binding: Binding) {
        bindings.add(binding)
    }

}

