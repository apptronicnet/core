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
import net.apptronic.core.debugError
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class AndroidView<T : ViewModel> : BindingContainer {

    @LayoutRes
    open var layoutResId: Int? = null

    private lateinit var viewModel: ViewModel
    private lateinit var view: View
    private lateinit var bindings: Bindings

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

    fun bindView(view: View, viewModel: ViewModel) {
        if (viewModel.boundView != null) {
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
        val stateKey = "view_state_${viewModel.id}"
        this.view = view
        this.viewModel = viewModel
        bindings = Bindings(viewModel, this)
        onBindView(view, viewModel as T)
        viewModel.getSavedState()?.let {
            val hierarchyState = it.get<SparseArray<Parcelable>>(stateKey)
            if (hierarchyState != null) {
                view.restoreHierarchyState(hierarchyState)
            }
        }
        viewModel.boundView = this
        viewModel.doOnUnbind {
            val hierarchyState = SparseArray<Parcelable>()
            view.saveHierarchyState(hierarchyState)
            viewModel.newSavedState().also {
                it.put(stateKey, hierarchyState)
            }
            bindings.unbind()
            viewModel.boundView = null
        }
    }

    protected abstract fun onBindView(view: View, viewModel: T)

    override fun onUnbind(action: () -> Unit) {
        bindings.onUnbind(action)
    }

    override infix fun add(binding: Binding) {
        bindings.add(binding)
    }

}

