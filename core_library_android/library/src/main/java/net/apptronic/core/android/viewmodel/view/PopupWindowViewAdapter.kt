package net.apptronic.core.android.viewmodel.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.IViewModel

interface PopupWindowViewAdapter : ViewAdapter {

    fun onCreatePopupWindow(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        context: Context
    ): PopupWindow {
        return PopupWindow(context)
    }

    fun onCreatePopupView(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        context: Context
    ): View {
        return viewBinder.onCreateView(viewBinder, context, LayoutInflater.from(context), null)
    }

    fun onAttachPopupView(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        popup: PopupWindow,
        view: View
    ) {
        popup.contentView = view
    }

    fun onShowPopupAsAnchor(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        popup: PopupWindow,
        anchor: View,
        transitionSpec: Any?
    ) {
        popup.showAsDropDown(anchor)
    }

    fun onShowPopupAtLocation(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        popup: PopupWindow,
        container: View,
        transitionSpec: Any?
    ) {
        popup.showAtLocation(container, 0, 0, 0)
    }

    fun onPopupShown(viewModel: IViewModel, viewBinder: ViewBinder<*>, popup: PopupWindow) {
        popup.setOnDismissListener {
            if (viewModel.isBound()) {
                viewModel.closeSelf()
            }
        }
    }

    fun onDismissPopup(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        popup: PopupWindow,
        transitionSpec: Any?
    ) {
        popup.dismiss()
    }

}