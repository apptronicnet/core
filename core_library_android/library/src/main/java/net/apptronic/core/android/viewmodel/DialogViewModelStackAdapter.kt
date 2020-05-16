package net.apptronic.core.android.viewmodel

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter

open class DialogViewModelStackAdapter(
    private val context: Context,
    private val androidViewFactory: AndroidViewFactory = AndroidViewFactory()
) : ViewModelStackAdapter() {

    fun bindings(setup: AndroidViewFactory.() -> Unit) {
        setup.invoke(androidViewFactory)
    }

    private class DialogAndView(
        val view: AndroidView<*>,
        val dialog: Dialog
    )

    private var current: DialogAndView? = null

    override fun onInvalidate(oldModel: ViewModel?, newModel: ViewModel?, transitionInfo: Any?) {
        val newAndroidView =
            if (newModel != null) androidViewFactory.getAndroidView(newModel) else null
        val next = if (newAndroidView != null && newModel != null) {
            val dialog = newAndroidView.onCreateDialog(context)
            val view = newAndroidView.onCreateDialogView(dialog)
            newAndroidView.onAttachDialogView(dialog, view)
            newAndroidView.bindView(view, newModel)
            DialogAndView(newAndroidView, dialog)
        } else null
        setDialog(next, transitionInfo)
        next?.let {
            it.view.onDialogShown(it.dialog, it.view.getViewModel())
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