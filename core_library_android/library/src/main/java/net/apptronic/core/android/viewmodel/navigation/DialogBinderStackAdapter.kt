package net.apptronic.core.android.viewmodel.navigation

import android.app.Dialog
import android.content.Context
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.view.DialogDelegate
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem
import net.apptronic.core.mvvm.viewmodel.navigation.adapters.SingleViewModelAdapter

open class DialogBinderStackAdapter(
    private val context: Context,
    private val viewBinderFactory: ViewBinderFactory
) : SingleViewModelAdapter {

    fun bindings(setup: ViewBinderFactory.() -> Unit) {
        setup.invoke(viewBinderFactory)
    }

    class DialogAndBinder(
        val viewBinder: ViewBinder<*>,
        val dialog: Dialog
    )

    private var current: DialogAndBinder? = null

    override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
        val newModel = item?.viewModel
        val newBinder =
            if (newModel != null) viewBinderFactory.getBinder(newModel) else null
        val next = if (newBinder != null && newModel != null) {
            val delegate = newBinder.getViewDelegate<DialogDelegate<*>>()
            val dialog = delegate.performCreateDialog(newModel, newBinder, context)
            val view = delegate.performCreateDialogView(newModel, newBinder, context)
            newBinder.performViewBinding(item, view)
            delegate.performAttachDialogView(newModel, newBinder, dialog, view)
            DialogAndBinder(newBinder, dialog)
        } else null
        setDialog(next, transitionInfo.spec)
        next?.let {
            val delegate = it.viewBinder.getViewDelegate<DialogDelegate<*>>()
            delegate.performDialogShown(it.viewBinder.getViewModel(), it.viewBinder, it.dialog)
        }
    }

    private fun setDialog(next: DialogAndBinder?, transitionSpec: Any?) {
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

    open fun onAdd(next: DialogAndBinder, transitionSpec: Any?) {
        val delegate = next.viewBinder.getViewDelegate<DialogDelegate<*>>()
        delegate.performShowDialog(next.viewBinder.getViewModel(), next.viewBinder, next.dialog)
    }

    open fun onReplace(previous: DialogAndBinder, next: DialogAndBinder, transitionSpec: Any?) {
        onRemove(previous, transitionSpec)
        onAdd(next, transitionSpec)
    }

    open fun onRemove(previous: DialogAndBinder, transitionSpec: Any?) {
        val delegate = previous.viewBinder.getViewDelegate<DialogDelegate<*>>()
        delegate.performDismissDialog(
            previous.viewBinder.getViewModel(), previous.viewBinder, previous.dialog
        )
    }

}