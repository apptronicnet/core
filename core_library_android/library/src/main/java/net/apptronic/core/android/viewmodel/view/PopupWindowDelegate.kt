package net.apptronic.core.android.viewmodel.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.ViewModel

open class PopupWindowDelegate<T : ViewModel> {

    @Suppress("UNCHECKED_CAST")
    fun performCreatePopupWindow(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        context: Context
    ): PopupWindow {
        return onCreatePopupWindow(viewModel as T, viewBinder as ViewBinder<T>, context)
    }

    protected open fun onCreatePopupWindow(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        context: Context
    ): PopupWindow {
        return PopupWindow(context)
    }

    @Suppress("UNCHECKED_CAST")
    fun performCreatePopupView(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        context: Context
    ): View {
        return onCreatePopupView(viewModel as T, viewBinder as ViewBinder<T>, context)
    }

    protected open fun onCreatePopupView(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        context: Context
    ): View {
        val layoutResId = viewBinder.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $viewBinder")
        return LayoutInflater.from(context).inflate(layoutResId, null, false)
    }

    @Suppress("UNCHECKED_CAST")
    fun performAttachPopupView(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        popup: PopupWindow,
        view: View
    ) {
        onAttachPopupView(viewModel as T, viewBinder as ViewBinder<T>, popup, view)
    }

    protected open fun onAttachPopupView(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        popup: PopupWindow,
        view: View
    ) {
        popup.contentView = view
    }

    @Suppress("UNCHECKED_CAST")
    fun performShowPopupAsAnchor(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        popup: PopupWindow,
        anchor: View,
        transitionSpec: Any?
    ) {
        onShowPopupAsAnchor(
            viewModel as T,
            viewBinder as ViewBinder<T>,
            popup,
            anchor,
            transitionSpec
        )
    }

    protected open fun onShowPopupAsAnchor(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        popup: PopupWindow,
        anchor: View,
        transitionSpec: Any?
    ) {
        popup.showAsDropDown(anchor)
    }

    @Suppress("UNCHECKED_CAST")
    fun performShowPopupAtLocation(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        popup: PopupWindow,
        container: View,
        transitionSpec: Any?
    ) {
        onShowPopupAtLocation(
            viewModel as T,
            viewBinder as ViewBinder<T>,
            popup,
            container,
            transitionSpec
        )
    }

    protected open fun onShowPopupAtLocation(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        popup: PopupWindow,
        container: View,
        transitionSpec: Any?
    ) {
        popup.showAtLocation(container, 0, 0, 0)
    }

    @Suppress("UNCHECKED_CAST")
    fun performPopupShown(viewModel: ViewModel, viewBinder: ViewBinder<*>, popup: PopupWindow) {
        onPopupShown(viewModel as T, viewBinder as ViewBinder<T>, popup)
    }

    protected open fun onPopupShown(viewModel: T, viewBinder: ViewBinder<T>, popup: PopupWindow) {
        popup.setOnDismissListener {
            if (viewModel.isBound()) {
                viewModel.closeSelf()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun performDismissPopup(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        popup: PopupWindow,
        transitionSpec: Any?
    ) {
        onDismissPopup(viewModel as T, viewBinder as ViewBinder<T>, popup, transitionSpec)
    }

    protected open fun onDismissPopup(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        popup: PopupWindow,
        transitionSpec: Any?
    ) {
        popup.dismiss()
    }


}