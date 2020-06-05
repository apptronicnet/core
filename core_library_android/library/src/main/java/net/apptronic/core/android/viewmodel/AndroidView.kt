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

private val SavedInstanceStateExtensionDescriptor = extensionDescriptor<SparseArray<Parcelable>>()
private val BoundViewExtensionsDescriptor = extensionDescriptor<AndroidView<*>>()

fun ViewModel.requireBoundView() {
    doOnVisible {
        if (extensions[BoundViewExtensionsDescriptor] == null) {
            debugError(Error("$this have no bound view"))
        }
    }
}

abstract class AndroidView<T : ViewModel> : BindingContainer {

    @LayoutRes
    open var layoutResId: Int? = null

    private var viewModel: ViewModel? = null
    private var view: View? = null
    private var bindings: Bindings? = null

    open fun onCreateView(container: ViewGroup): View {
        val layoutResId = this.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $this")
        return LayoutInflater.from(container.context).inflate(layoutResId, container, false)
    }

    open fun onAttachView(container: ViewGroup) {
        container.isSaveFromParentEnabled = false
        container.addView(onCreateView(container))
    }

    open fun onCreateActivityView(activity: Activity): View {
        val layoutResId = this.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $this")
        return activity.layoutInflater.inflate(layoutResId, null)
    }

    open fun onCreateDialog(context: Context): Dialog {
        return Dialog(context)
    }

    open fun onDialogShown(dialog: Dialog, viewModel: ViewModel) {
        dialog.setOnDismissListener {
            viewModel.closeSelf()
        }
    }

    open fun onCreateDialogView(dialog: Dialog): View {
        val layoutResId = this.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $this")
        return LayoutInflater.from(dialog.context).inflate(layoutResId, null, false)
    }

    open fun onAttachDialogView(dialog: Dialog, view: View) {
        dialog.setContentView(view)
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

    fun bindView(view: View, viewModel: ViewModel) {
        if (viewModel.extensions[BoundViewExtensionsDescriptor] != null) {
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
        viewModel.extensions[BoundViewExtensionsDescriptor] = this
        viewModel.doOnUnbind {
            val hierarchyState = SparseArray<Parcelable>()
            view.saveHierarchyState(hierarchyState)
            viewModel.extensions[SavedInstanceStateExtensionDescriptor] = hierarchyState
            getBindings().unbind()
            bindings = null
            viewModel.extensions.remove(BoundViewExtensionsDescriptor)
        }
    }

    protected abstract fun onBindView(view: View, viewModel: T)

    override fun onUnbind(action: () -> Unit) {
        getBindings().onUnbind(action)
    }

    override infix fun add(binding: Binding) {
        getBindings().add(binding)
    }

}

