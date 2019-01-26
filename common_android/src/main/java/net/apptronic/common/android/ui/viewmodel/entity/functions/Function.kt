package net.apptronic.common.android.ui.viewmodel.entity.functions

import net.apptronic.common.android.ui.viewmodel.entity.ViewModelProperty

abstract class Function<T> : Predicate<T> {

    abstract fun calculate(): T

    override fun getPredicateValue(): T {
        return calculate()
    }

}

fun <T, A> predicateFunction(
    source: Predicate<A>,
    method: (A) -> T
): Function<T> {
    return SingleFunction(source, method)
}

fun <T> predicateArrayFunction(
    source: Array<Predicate<*>>,
    method: (Array<Any>) -> T
): Function<T> {
    return ArrayFunction(source, method)
}

fun <T, A, B> predicateFunction(
    left: Predicate<A>,
    right: Predicate<B>,
    method: (A, B) -> T
): Function<T> {
    return DoubleFunction(left, right, method)
}

private class SingleFunction<T, X>(
    private val source: Predicate<X>,
    private val method: (X) -> T
) : Function<T>() {

    override fun getPredicateSubjects(): Set<ViewModelProperty<*>> {
        return source.getPredicateSubjects()
    }

    override fun calculate(): T {
        return method(source.getPredicateValue())
    }

}

private class DoubleFunction<T, A, B>(
    private val left: Predicate<A>,
    private val right: Predicate<B>,
    private val method: (A, B) -> T
) : Function<T>() {

    override fun getPredicateSubjects(): Set<ViewModelProperty<*>> {
        return mutableSetOf<ViewModelProperty<*>>().apply {
            addAll(left.getPredicateSubjects())
            addAll(right.getPredicateSubjects())
        }
    }

    override fun calculate(): T {
        return method(left.getPredicateValue(), right.getPredicateValue())
    }

}

private class ArrayFunction<T>(
    private val source: Array<Predicate<*>>,
    private val method: (Array<Any>) -> T
) : Function<T>() {

    override fun getPredicateSubjects(): Set<ViewModelProperty<*>> {
        return mutableSetOf<ViewModelProperty<*>>().apply {
            source.forEach {
                addAll(it.getPredicateSubjects())
            }
        }
    }

    override fun calculate(): T {
        return method(source.map { it.getPredicateValue() as Any }.toTypedArray())
    }

}