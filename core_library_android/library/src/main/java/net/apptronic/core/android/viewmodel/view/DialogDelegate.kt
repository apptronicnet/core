package net.apptronic.core.android.viewmodel.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.ViewModel

open class DialogDelegate<T : ViewModel> {

    @Suppress("UNCHECKED_CAST")
    fun performCreateDialog(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        context: Context
    ): Dialog {
        return onCreateDialog(viewModel as T, viewBinder as ViewBinder<T>, context)
    }

    /**
     * Create [Dialog] which should show [View]
     */
    protected open fun onCreateDialog(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        context: Context
    ): Dialog {
        return Dialog(context)
    }

    @Suppress("UNCHECKED_CAST")
    fun performCreateDialogView(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        context: Context
    ): View {
        return onCreateDialogView(viewModel as T, viewBinder as ViewBinder<T>, context)
    }

    /**
     * Create [View] for [Dialog]
     */
    protected open fun onCreateDialogView(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        context: Context
    ): View {
        val layoutResId = viewBinder.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $viewBinder")
        return LayoutInflater.from(context).inflate(layoutResId, null, false)
    }

    @Suppress("UNCHECKED_CAST")
    fun performAttachDialogView(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        dialog: Dialog,
        view: View
    ) {
        onAttachDialogView(viewModel as T, viewBinder as ViewBinder<T>, dialog, view)
    }

    /**
     * Attach [View] to [dialog]
     */
    protected open fun onAttachDialogView(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        dialog: Dialog,
        view: View
    ) {
        dialog.setContentView(view)
    }

    @Suppress("UNCHECKED_CAST")
    fun performShowDialog(viewModel: ViewModel, viewBinder: ViewBinder<*>, dialog: Dialog) {
        onShowDialog(viewModel as T, viewBinder as ViewBinder<T>, dialog)
    }

    protected open fun onShowDialog(viewModel: T, viewBinder: ViewBinder<T>, dialog: Dialog) {
        dialog.show()
    }

    @Suppress("UNCHECKED_CAST")
    fun performDismissDialog(viewModel: ViewModel, viewBinder: ViewBinder<*>, dialog: Dialog) {
        onDismissDialog(viewModel as T, viewBinder as ViewBinder<T>, dialog)
    }

    protected open fun onDismissDialog(viewModel: T, viewBinder: ViewBinder<T>, dialog: Dialog) {
        dialog.dismiss()
    }

    @Suppress("UNCHECKED_CAST")
    fun performDialogShown(viewModel: ViewModel, viewBinder: ViewBinder<*>, dialog: Dialog) {
        onDialogShown(viewModel as T, viewBinder as ViewBinder<T>, dialog)
    }

    /**
     * Called when [dialog] for [viewModel] is shown.
     *
     * May be called many times after save/restore [View] in case of [Activity] recreation. Each
     * time new instance of [dialog] is created for same [viewModel]
     */
    protected open fun onDialogShown(viewModel: T, viewBinder: ViewBinder<T>, dialog: Dialog) {
        dialog.setOnDismissListener {
            if (viewModel.isBound()) {
                viewModel.closeSelf()
            }
        }
    }

}