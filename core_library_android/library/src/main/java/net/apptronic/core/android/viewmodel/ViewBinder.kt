package net.apptronic.core.android.viewmodel

import android.app.Activity
import android.app.Dialog
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import net.apptronic.core.android.viewmodel.view.ViewAdapter
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.debugError
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem

private val SavedInstanceStateExtensionDescriptor = extensionDescriptor<SparseArray<Parcelable>>()
private val ViewBinderExtensionsDescriptor = extensionDescriptor<ViewBinder<*>>()

fun IViewModel.requireBoundView() {
    doOnVisible {
        if (extensions[ViewBinderExtensionsDescriptor] == null) {
            debugError(Error("$this have no ViewBinder"))
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

    private var item: ViewModelItem? = null
    private var viewModel: T? = null
    private var view: View? = null
    private var bindings: Bindings? = null

    fun getItem(): ViewModelItem {
        return item ?: throw IllegalStateException("No ViewModelItem for $this")
    }

    fun getViewModel(): T {
        return viewModel ?: throw IllegalStateException("No viewModel bound for $this")
    }

    fun getView(): View {
        return view ?: throw IllegalStateException("No view bound for $this")
    }

    internal fun getBindings(): Bindings {
        return bindings ?: throw IllegalStateException("No bindings for $this")
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
     * Bind [view] to [viewModel]
     */
    @Suppress("UNCHECKED_CAST")
    fun performViewBinding(item: ViewModelItem, view: View) {
        preCheck(item.viewModel)
        this.item = item
        performViewBindingInternal(item.viewModel, view)

    }

    private fun performViewBindingInternal(viewModel: IViewModel, view: View) {
        this.view = view
        this.viewModel = viewModel as T
        bindings = Bindings(viewModel, this)
        onBindView(view, viewModel)

        viewModel.extensions[SavedInstanceStateExtensionDescriptor]?.let {
            view.restoreHierarchyState(it)
        }
        viewModel.extensions[ViewBinderExtensionsDescriptor] = this
        viewModel.doOnUnbind {
            val hierarchyState = SparseArray<Parcelable>()
            view.saveHierarchyState(hierarchyState)
            viewModel.extensions[SavedInstanceStateExtensionDescriptor] = hierarchyState
            getBindings().unbind()
            bindings = null
            viewModel.extensions.remove(ViewBinderExtensionsDescriptor)
        }
    }

    /**
     * Called when [view] is binding to the [viewModel]. At this time [viewModel] lifecycle
     * is in stage [ViewModelLifecycle.STAGE_BOUND]
     */
    protected abstract fun onBindView(view: View, viewModel: T)

    final override fun onUnbind(action: () -> Unit) {
        getBindings().onUnbind(action)
    }

    final override fun add(binding: Binding) {
        getBindings().add(binding)
    }

}

