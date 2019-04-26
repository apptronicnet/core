package net.apptronic.core.android.viewmodel

import android.view.View
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class ViewModelBinding<T : ViewModel>(
    val view: View,
    val viewModel: T
) {

    private val onUnbindActions = mutableListOf<() -> Unit>()

    internal fun bind() {
        onBind()
        viewModel.getLifecycle().onExitFromActiveStage {
            onUnbindActions.forEach { it.invoke() }
            onUnbindActions.clear()
        }
    }

    fun doOnUnbind(action: () -> Unit) {
        onUnbindActions.add(action)
    }

    protected abstract fun onBind()

    fun <ViewType : View, DataType, PredicateType : Predicate<DataType>> ViewType.bindTo(
        binding: ViewToPredicateBinding<ViewType, DataType, PredicateType>,
        predicate: PredicateType
    ) {
        binding.performBinding(this@ViewModelBinding, this, predicate)
    }

    fun <ViewType : View, DataType> ViewType.bindTo(
        binding: ViewToActionBinding<ViewType, DataType>,
        target: (DataType) -> Unit
    ) {
        binding.performBinding(this@ViewModelBinding, this, target)
    }

    fun <ViewType : View> ViewType.bindTo(
        binding: ViewToActionBinding<ViewType, Unit>,
        target: () -> Unit
    ) {
        binding.performBinding(this@ViewModelBinding, this) {
            target.invoke()
        }
    }

}