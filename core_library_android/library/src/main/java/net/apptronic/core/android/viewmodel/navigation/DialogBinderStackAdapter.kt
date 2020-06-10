package net.apptronic.core.android.viewmodel.navigation

import android.app.Dialog
import android.content.Context
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter

open class DialogBinderStackAdapter(
    private val context: Context,
    private val viewBinderFactory: ViewBinderFactory = ViewBinderFactory()
) : ViewModelStackAdapter() {

    fun bindings(setup: ViewBinderFactory.() -> Unit) {
        setup.invoke(viewBinderFactory)
    }

    private class DialogAndView(
        val viewBinder: ViewBinder<*>,
        val dialog: Dialog
    )

    private var current: DialogAndView? = null

    override fun onInvalidate(oldModel: ViewModel?, newModel: ViewModel?, transitionInfo: Any?) {
        val newBinder =
            if (newModel != null) viewBinderFactory.getBinder(newModel) else null
        val next = if (newBinder != null && newModel != null) {
            val dialog = newBinder.onCreateDialog(context)
            val view = newBinder.onCreateDialogView(dialog)
            newBinder.onAttachDialogView(dialog, view)
            newBinder.bindView(view, newModel)
            DialogAndView(
                newBinder,
                dialog
            )
        } else null
        setDialog(next, transitionInfo)
        next?.let {
            it.viewBinder.onDialogShown(it.dialog, it.viewBinder.getViewModel())
        }
    }

    private fun setDialog(next: DialogAndView?, transitionInfo: Any?) {
        val old = current
        current = next
        if (old != null && next != null) {
            onReplace(old.dialog, next.dialog, transitionInfo)
        } else if (next != null) {
            onAdd(next.dialog, transitionInfo)
        } else if (old != null) {
            onRemove(old.dialog, transitionInfo)
        }
    }

    open fun onAdd(next: Dialog, transitionInfo: Any?) {
        next.show()
    }

    open fun onReplace(previous: Dialog, next: Dialog, transitionInfo: Any?) {
        onRemove(previous, transitionInfo)
        onAdd(next, transitionInfo)
    }

    open fun onRemove(previous: Dialog, transitionInfo: Any?) {
        previous.dismiss()
    }

}