package net.apptronic.core.component.entity.functions

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.base.DistinctUntilChangedStorePredicate
import net.apptronic.core.component.entity.base.ValueHolder

abstract class Function<T> : DistinctUntilChangedStorePredicate<T>()

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
    source: Predicate<X>,
    private val method: (X) -> T
) : Function<T>() {

    private var sourceValue: ValueHolder<X>? = null

    init {
        source.subscribe {
            sourceValue = ValueHolder(it)
            calculate()
        }
    }

    private fun calculate() {
        val sourceValue = this.sourceValue
        if (sourceValue != null) {
            val result = method(sourceValue.value)
            update(result)
        }
    }

}

private class DoubleFunction<T, A, B>(
    left: Predicate<A>,
    right: Predicate<B>,
    private val method: (A, B) -> T
) : Function<T>() {

    private var leftValue: ValueHolder<A>? = null
    private var rightValue: ValueHolder<B>? = null

    init {
        left.subscribe {
            leftValue = ValueHolder(it)
            calculate()
        }
        right.subscribe {
            rightValue = ValueHolder(it)
            calculate()
        }
    }

    private fun calculate() {
        val leftValue = this.leftValue
        val rightValue = this.rightValue
        if (leftValue != null && rightValue != null) {
            val result = method(leftValue.value, rightValue.value)
            update(result)
        }
    }

}

private class ArrayFunction<T>(
    private val sources: Array<Predicate<*>>,
    private val method: (Array<Any>) -> T
) : Function<T>() {

    private val sourceValues = arrayOfNulls<ValueHolder<*>>(sources.size)

    init {
        for (i in 0 until sources.size) {
            i.let { index ->
                sources[index].subscribe {
                    sourceValues[index] =
                        ValueHolder(it)
                    calculate()
                }
            }
        }
    }

    private fun calculate() {
        val sourceValues = this.sourceValues.clone()
        if (sourceValues.all { it != null }) {
            val params = sourceValues.map { it!!.value }
            val result = method(arrayOf(params))
            update(result)
        }
    }

}