package net.apptronic.core.android.viewmodel.navigation

import android.content.Context
import android.view.View
import android.widget.PopupWindow
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.view.PopupWindowDelegate
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem
import net.apptronic.core.mvvm.viewmodel.navigation.adapters.SingleViewModelAdapter

interface PopupAnchorProvider {

    fun provideAnchorForPopup(viewModel: IViewModel): View?

}

open class PopupBinderStackAdapter(
    private val context: Context,
    private val container: View,
    private val anchorProvider: PopupAnchorProvider,
    private val viewBinderFactory: ViewBinderFactory
) : SingleViewModelAdapter {

    fun bindings(setup: ViewBinderFactory.() -> Unit) {
        setup.invoke(viewBinderFactory)
    }

    class PopupAndBinder(
        val viewBinder: ViewBinder<*>,
        val popupWindow: PopupWindow
    )

    private var current: PopupAndBinder? = null

    override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
        val newModel = item?.viewModel
        val newBinder =
            if (newModel != null) viewBinderFactory.getBinder(newModel) else null
        val next = if (newBinder != null && newModel != null) {
            val delegate = newBinder.getViewDelegate<PopupWindowDelegate<*>>()
            val popup = delegate.performCreatePopupWindow(newModel, newBinder, context)
            val view = delegate.performCreatePopupView(newModel, newBinder, context)
            delegate.performAttachPopupView(newModel, newBinder, popup, view)
            newBinder.performViewBinding(newModel, view)
            PopupAndBinder(newBinder, popup)
        } else null
        setDialog(next, transitionInfo.spec)
        next?.let {
            val delegate = it.viewBinder.getViewDelegate<PopupWindowDelegate<*>>()
            delegate.performPopupShown(it.viewBinder.getViewModel(), it.viewBinder, it.popupWindow)
        }
    }

    private fun setDialog(next: PopupAndBinder?, transitionSpec: Any?) {
        val old = current
        current = next
        if (old != null && next != null) {
            onReplace(old, next, transitionSpec)
        } else if (next != null) {
            onAdd(next, transitionSpec)
        } else if (old != null) {
            onRemove(old, transitionSpec)
        }
    }

    open fun onAdd(next: PopupAndBinder, transitionSpec: Any?) {
        val delegate = next.viewBinder.getViewDelegate<PopupWindowDelegate<*>>()
        val anchor = anchorProvider.provideAnchorForPopup(next.viewBinder.getViewModel())
        if (anchor != null) {
            delegate.performShowPopupAsAnchor(
                next.viewBinder.getViewModel(),
                next.viewBinder,
                next.popupWindow,
                anchor,
                transitionSpec
            )
        } else {
            delegate.performShowPopupAtLocation(
                next.viewBinder.getViewModel(),
                next.viewBinder,
                next.popupWindow,
                container,
                transitionSpec
            )
        }
    }

    open fun onReplace(previous: PopupAndBinder, next: PopupAndBinder, transitionSpec: Any?) {
        onRemove(previous, transitionSpec)
        onAdd(next, transitionSpec)
    }

    open fun onRemove(previous: PopupAndBinder, transitionSpec: Any?) {
        val delegate = previous.viewBinder.getViewDelegate<PopupWindowDelegate<*>>()
        delegate.performDismissPopup(
            previous.viewBinder.getViewModel(),
            previous.viewBinder,
            previous.popupWindow,
            transitionSpec
        )
    }

}