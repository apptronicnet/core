package net.apptronic.core.android.viewmodel.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.IViewModel

interface DialogViewAdapter : ViewAdapter {

    /**
     * Create [Dialog] which should show [View]
     */
    fun onCreateDialog(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        context: Context
    ): Dialog {
        return Dialog(context)
    }

    /**
     * Create [View] for [Dialog]
     */
    fun onCreateDialogView(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        context: Context
    ): View {
        return viewBinder.onCreateView(viewBinder, context, LayoutInflater.from(context), null)
    }

    /**
     * Attach [View] to [dialog]
     */
    fun onAttachDialogView(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        dialog: Dialog,
        view: View
    ) {
        dialog.setContentView(view)
    }

    fun onShowDialog(viewModel: IViewModel, viewBinder: ViewBinder<*>, dialog: Dialog) {
        dialog.show()
    }

    fun onDismissDialog(viewModel: IViewModel, viewBinder: ViewBinder<*>, dialog: Dialog) {
        dialog.dismiss()
    }

    /**
     * Called when [dialog] for [viewModel] is shown.
     *
     * May be called many times after save/restore [View] in case of [Activity] recreation. Each
     * time new instance of [dialog] is created for same [viewModel]
     */
    fun onDialogShown(viewModel: IViewModel, viewBinder: ViewBinder<*>, dialog: Dialog) {
        dialog.setOnDismissListener {
            if (viewModel.isBound()) {
                viewModel.closeSelf()
            }
        }
    }

}