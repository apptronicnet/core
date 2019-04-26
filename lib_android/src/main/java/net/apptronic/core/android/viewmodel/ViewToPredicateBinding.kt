package net.apptronic.core.android.viewmodel


import android.view.View
import net.apptronic.core.component.entity.Predicate

fun <ViewType : View, DataType, PredicateType : Predicate<DataType>> customBinding(
    bindingAction: ViewModelBinding<*>.(ViewType, PredicateType) -> Unit
): ViewToPredicateBinding<ViewType, DataType, PredicateType> {
    return CustomViewToPredicateBinding(bindingAction)
}

interface ViewToPredicateBinding<ViewType : View, DataType, PredicateType : Predicate<DataType>> {

    fun performBinding(binding: ViewModelBinding<*>, view: ViewType, target: PredicateType)

}

private class CustomViewToPredicateBinding<ViewType : View, DataType, PredicateType : Predicate<DataType>>(
    val bindingAction: ViewModelBinding<*>.(ViewType, PredicateType) -> Unit
) : ViewToPredicateBinding<ViewType, DataType, PredicateType> {

    override fun performBinding(
        binding: ViewModelBinding<*>,
        view: ViewType,
        target: PredicateType
    ) {
        binding.bindingAction(view, target)
    }

}