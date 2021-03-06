package net.apptronic.core.android.viewmodel.navigation

import android.app.Dialog
import android.content.Context
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.android.viewmodel.view.DefaultDialogViewAdapter
import net.apptronic.core.android.viewmodel.view.DialogViewAdapter
import net.apptronic.core.viewmodel.navigation.TransitionInfo
import net.apptronic.core.viewmodel.navigation.ViewModelItem
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelAdapter

open class DialogBinderStackAdapter(
    private val context: Context,
    private val viewBinderAdapter: ViewBinderAdapter
) : SingleViewModelAdapter {

    fun bindings(setup: ViewBinderAdapter.() -> Unit) {
        setup.invoke(viewBinderAdapter)
    }

    class DialogAndBinder(
        val viewBinder: ViewBinder<*>,
        val dialog: Dialog
    )

    private var current: DialogAndBinder? = null

    override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
        val newModel = item?.viewModel
        val newBinder =
            if (newModel != null) viewBinderAdapter.getBinder(newModel) else null
        val next = if (newBinder != null && newModel != null) {
            val viewAdapter = newBinder as? DialogViewAdapter ?: DefaultDialogViewAdapter
            val dialog = viewAdapter.onCreateDialog(newModel, newBinder, context)
            val view = viewAdapter.onCreateDialogView(newModel, newBinder, context)
            newBinder.performViewBinding(item, view)
            viewAdapter.onAttachDialogView(newModel, newBinder, dialog, view)
            DialogAndBinder(newBinder, dialog)
        } else null
        setDialog(next, transitionInfo.spec)
        next?.let {
            val viewAdapter = it.viewBinder as? DialogViewAdapter ?: DefaultDialogViewAdapter
            viewAdapter.onDialogShown(it.viewBinder.viewModel, it.viewBinder, it.dialog)
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
        val viewAdapter = next.viewBinder as? DialogViewAdapter ?: DefaultDialogViewAdapter
        viewAdapter.onShowDialog(next.viewBinder.viewModel, next.viewBinder, next.dialog)
    }

    open fun onReplace(previous: DialogAndBinder, next: DialogAndBinder, transitionSpec: Any?) {
        onRemove(previous, transitionSpec)
        onAdd(next, transitionSpec)
    }

    open fun onRemove(previous: DialogAndBinder, transitionSpec: Any?) {
        val viewAdapter = previous.viewBinder as? DialogViewAdapter ?: DefaultDialogViewAdapter
        viewAdapter.onDismissDialog(
            previous.viewBinder.viewModel, previous.viewBinder, previous.dialog
        )
    }

}