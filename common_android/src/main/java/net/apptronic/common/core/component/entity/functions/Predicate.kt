package net.apptronic.common.core.component.entity.functions

import net.apptronic.common.core.component.entity.ViewModelProperty
import net.apptronic.common.core.mvvm.viewmodel.extensions.forEachChangeAnyOf

interface Predicate<T> {

    fun getPredicateSubjects(): Set<ViewModelProperty<*>>

    fun getPredicateValue(): T

}

fun <T> Predicate<T>.subscribe(action: (T) -> Unit) {
    val allSubjects = getPredicateSubjects()
    forEachChangeAnyOf(*allSubjects.toTypedArray()) {
        action(getPredicateValue())
    }
}

class ValuePredicate<T>(
    private val value: T
) : Predicate<T> {

    override fun getPredicateSubjects(): Set<ViewModelProperty<*>> {
        return emptySet()
    }

    override fun getPredicateValue(): T {
        return value
    }

}

