package net.apptronic.core.android.viewmodel

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class AndroidView<T : ViewModel> : BindingContainer {

    @LayoutRes
    open val layoutResId: Int? = null

    private var viewModel: ViewModel? = null
    private var view: View? = null

    private var bindings: Bindings? = null
    private val bindingList = mutableListOf<Binding>()

    open fun onCreateView(container: ViewGroup): View {
        val layoutResId = this.layoutResId
                ?: throw IllegalStateException("[layoutResId] is not specified for $this")
        return LayoutInflater.from(container.context).inflate(layoutResId, container, false)
    }

    open fun onAttachView(container: ViewGroup) {
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
        this.view = view
        this.viewModel = viewModel
        bindings = Bindings(viewModel, this)
        onBindView(view, viewModel as T)
        viewModel.doOnUnbind {
            bindings!!.unbind()
            bindings = null
        }
    }

    protected abstract fun onBindView(view: View, viewModel: T)

    override fun onUnbind(action: () -> Unit) {
        bindings!!.onUnbind(action)
    }

    override infix fun add(binding: Binding) {
        bindings!!.add(binding)
    }

    override operator fun Binding.unaryPlus() {
        bindings!!.add(this)
    }

}

