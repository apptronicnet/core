package net.apptronic.core.android.viewmodel

import android.view.View

fun <ViewType : View, DataType> customAction(
    bindingAction: ViewModelBinding<*>.(ViewType, (DataType) -> Unit) -> Unit
): ViewToActionBinding<ViewType, DataType> {
    return CustomViewToActionBinding(bindingAction)
}

fun <ViewType : View> genericAction(
    bindingAction: ViewModelBinding<*>.(ViewType, () -> Unit) -> Unit
): ViewToActionBinding<ViewType, Unit> {
    return object : ViewToGenericActionBinding<ViewType>() {
        override fun onPerformBinding(
            binding: ViewModelBinding<*>,
            view: ViewType,
            target: () -> Unit
        ) {
            bindingAction.invoke(binding, view, target)
        }
    }
}

interface ViewToActionBinding<ViewType : View, DataType> {

    fun performBinding(binding: ViewModelBinding<*>, view: ViewType, target: (DataType) -> Unit)

}

private class CustomViewToActionBinding<ViewType : View, DataType>(
    private val bindingAction: ViewModelBinding<*>.(ViewType, (DataType) -> Unit) -> Unit
) : ViewToActionBinding<ViewType, DataType> {

    override fun performBinding(
        binding: ViewModelBinding<*>,
        view: ViewType,
        target: (DataType) -> Unit
    ) {
        bindingAction.invoke(binding, view, target)
    }

}

abstract class ViewToGenericActionBinding<ViewType : View> : ViewToActionBinding<ViewType, Unit> {

    final override fun performBinding(
        binding: ViewModelBinding<*>,
        view: ViewType,
        target: (Unit) -> Unit
    ) {
        onPerformBinding(binding, view) {
            target.invoke(Unit)
        }
    }

    abstract fun onPerformBinding(binding: ViewModelBinding<*>, view: ViewType, target: () -> Unit)

}
