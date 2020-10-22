package net.apptronic.core.android.viewmodel.navigation

import android.content.Context
import android.view.View
import android.widget.PopupWindow
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.view.DefaultPopupWindowViewAdapter
import net.apptronic.core.android.viewmodel.view.PopupWindowViewAdapter
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
            val viewAdapter = newBinder as? PopupWindowViewAdapter ?: DefaultPopupWindowViewAdapter
            val popup = viewAdapter.onCreatePopupWindow(newModel, newBinder, context)
            val view = viewAdapter.onCreatePopupView(newModel, newBinder, context)
            viewAdapter.onAttachPopupView(newModel, newBinder, popup, view)
            newBinder.performViewBinding(newModel, view)
            PopupAndBinder(newBinder, popup)
        } else null
        setDialog(next, transitionInfo.spec)
        next?.let {
            val viewAdapter =
                it.viewBinder as? PopupWindowViewAdapter ?: DefaultPopupWindowViewAdapter
            viewAdapter.onPopupShown(it.viewBinder.viewModel, it.viewBinder, it.popupWindow)
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
        val viewAdapter =
            next.viewBinder as? PopupWindowViewAdapter ?: DefaultPopupWindowViewAdapter
        val anchor = anchorProvider.provideAnchorForPopup(next.viewBinder.viewModel)
        if (anchor != null) {
            viewAdapter.onShowPopupAsAnchor(
                next.viewBinder.viewModel,
                next.viewBinder,
                next.popupWindow,
                anchor,
                transitionSpec
            )
        } else {
            viewAdapter.onShowPopupAtLocation(
                next.viewBinder.viewModel,
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
        val viewAdapter =
            previous.viewBinder as? PopupWindowViewAdapter ?: DefaultPopupWindowViewAdapter
        viewAdapter.onDismissPopup(
            previous.viewBinder.viewModel,
            previous.viewBinder,
            previous.popupWindow,
            transitionSpec
        )
    }

}