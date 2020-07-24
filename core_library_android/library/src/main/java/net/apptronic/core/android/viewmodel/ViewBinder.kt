package net.apptronic.core.android.viewmodel

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Parcelable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.debugError
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle

private val SavedInstanceStateExtensionDescriptor = extensionDescriptor<SparseArray<Parcelable>>()
private val ViewBinderExtensionsDescriptor = extensionDescriptor<ViewBinder<*>>()

fun ViewModel.requireBoundView() {
//    doOnVisible {
//        if (extensions[ViewBinderExtensionsDescriptor] == null) {
//            debugError(Error("$this have no ViewBinder"))
//        }
//    }
}

/**
 * Responsible for creating [View] and binding it to [ViewModel]. Generally implements "View" layer.
 *
 * There are several methods for usage with [Activity], [Dialog] or generic usage as [View] under
 * some [ViewGroup] container.
 */
abstract class ViewBinder<T : ViewModel> : BindingContainer {

    /**
     * Specify layout resource to be used for creation or [View] using default implementation.
     * If null - should override each method [onCreateView], [onCreateActivityView],
     * [onCreateDialogView] to be used for corresponding binding or navigator.
     */
    @LayoutRes
    open var layoutResId: Int? = null

    private var viewModel: ViewModel? = null
    private var view: View? = null
    private var bindings: Bindings? = null

    /**
     * Create [View] for adding to [container]
     */
    open fun onCreateView(context: Context): View {
        val layoutResId = this.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $this")
        return LayoutInflater.from(context).inflate(layoutResId, null, false)
    }

    /**
     * Create [View] for adding to [container]
     */
    open fun onCreateView(container: ViewGroup): View {
        val layoutResId = this.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $this")
        return LayoutInflater.from(container.context).inflate(layoutResId, container, false)
    }

    /**
     * Attach [view] to a [container] meaning it should be added and, if needed, perform additional
     * actions
     */
    open fun onAttachView(view: View, container: ViewGroup) {
        container.isSaveFromParentEnabled = false
        container.addView(view)
    }

    /**
     * Create [View] for [activity]
     */
    open fun onCreateActivityView(activity: Activity): View {
        val layoutResId = this.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $this")
        return activity.layoutInflater.inflate(layoutResId, null)
    }

    /**
     * Create [Dialog] which should show [View]
     */
    open fun onCreateDialog(context: Context): Dialog {
        return Dialog(context)
    }

    /**
     * Create [View] for [Dialog]
     */
    open fun onCreateDialogView(dialog: Dialog): View {
        val layoutResId = this.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $this")
        return LayoutInflater.from(dialog.context).inflate(layoutResId, null, false)
    }

    /**
     * Attach [View] to [dialog]
     */
    open fun onAttachDialogView(dialog: Dialog, view: View) {
        dialog.setContentView(view)
    }

    /**
     * Called when [dialog] for [viewModel] is shown.
     *
     * May be called many times after save/restore [View] in case of [Activity] recreation. Each
     * time new instance of [dialog] is created for same [viewModel]
     */
    open fun onDialogShown(dialog: Dialog, viewModel: ViewModel) {
        dialog.setOnDismissListener {
            viewModel.closeSelf()
        }
    }

    internal fun getViewModel(): ViewModel {
        return viewModel ?: throw IllegalStateException("No viewModel bound for $this")
    }

    internal fun getView(): View {
        return view ?: throw IllegalStateException("No view bound for $this")
    }

    internal fun getBindings(): Bindings {
        return bindings ?: throw IllegalStateException("No bindings for $this")
    }

    /**
     * Bind [view] to [viewModel]
     */
    fun performViewBinding(view: View, viewModel: ViewModel) {
        if (viewModel.extensions[ViewBinderExtensionsDescriptor] != null) {
            debugError(Error("$viewModel already have bound view!!!"))
        }
        if (!viewModel.isStateBound()) {
            debugError(
                Error(
                    "$viewModel in stage ${viewModel.context.lifecycle.getActiveStage()
                        ?.getStageName()}"
                )
            )
        }
        this.view = view
        this.viewModel = viewModel
        bindings = Bindings(viewModel, this)
        onBindView(view, viewModel as T)

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

