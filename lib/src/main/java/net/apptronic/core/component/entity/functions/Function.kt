package net.apptronic.core.component.entity.functions

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.*

abstract class Function<T> : EntityValue<T> {

    private val subject = BehaviorSubject<T>()
    private val observable = subject.distinctUntilChanged()

    internal fun update(value: T) {
        subject.update(value)
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return observable.subscribe(observer).bindContext(getContext())
    }

    override fun get(): T {
        return subject.getValue().get()
    }

    override fun getOrNull(): T? {
        return subject.getValue().getOrNull()
    }

}

fun <T, A> entityFunction(
    source: Entity<A>,
    method: (A) -> T
): Function<T> {
    return SingleFunction(source, method)
}

fun <T> entityArrayFunction(
    source: Array<Entity<*>>,
    method: (Array<Any?>) -> T
): Function<T> {
    return ArrayFunction(source, method)
}

fun <T, A, B> entityFunction(
    left: Entity<A>,
    right: Entity<B>,
    method: (A, B) -> T
): Function<T> {
    return DoubleFunction(left, right, method)
}

fun <T, A, B, C> entityFunction(
    a: Entity<A>,
    b: Entity<B>,
    c: Entity<C>,
    method: (A, B, C) -> T
): Function<T> {
    return ArrayFunction(arrayOf(a, b, c)) {
        method(it[0] as A, it[1] as B, it[2] as C)
    }
}

fun <T, A, B, C, D> entityFunction(
    a: Entity<A>,
    b: Entity<B>,
    c: Entity<C>,
    d: Entity<D>,
    method: (A, B, C, D) -> T
): Function<T> {
    return ArrayFunction(arrayOf(a, b, c, d)) {
        method(it[0] as A, it[1] as B, it[2] as C, it[3] as D)
    }
}

fun <T, A, B, C, D, E> entityFunction(
    a: Entity<A>,
    b: Entity<B>,
    c: Entity<C>,
    d: Entity<D>,
    e: Entity<E>,
    method: (A, B, C, D, E) -> T
): Function<T> {
    return ArrayFunction(arrayOf(a, b, c, d, e)) {
        method(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E)
    }
}

private class SingleFunction<T, X>(
    private val sourceEntity: Entity<X>,
    private val method: (X) -> T
) : Function<T>() {

    private var sourceSubject = BehaviorSubject<X>()

    override fun getContext(): Context {
        return sourceEntity.getContext()
    }

    init {
        sourceEntity.subscribe {
            sourceSubject.update(it)
            calculate()
        }
    }

    private fun calculate() {
        val source = sourceSubject.getValue()
        if (source != null) {
            val result = method(source.value)
            update(result)
        }
    }

}

private fun collectContext(vararg entities: Entity<*>): Context {
    val context = entities[0].getContext().getToken()
    entities.forEach {
        if (context !== it.getContext().getToken()) {
            throw IllegalArgumentException("Function cannot use arguments from different contexts")
        }
    }
    return context
}

private class DoubleFunction<T, A, B>(
    left: Entity<A>,
    right: Entity<B>,
    private val method: (A, B) -> T
) : Function<T>() {

    private var leftValue = BehaviorSubject<A>()
    private var rightValue = BehaviorSubject<B>()
    private val context = collectContext(left, right)

    init {
        left.subscribe {
            leftValue.update(it)
            calculate()
        }
        right.subscribe {
            rightValue.update(it)
            calculate()
        }
    }

    override fun getContext(): Context {
        return context
    }

    private fun calculate() {
        val left = leftValue.getValue()
        val right = rightValue.getValue()
        if (left != null && right != null) {
            val result = method(left.value, right.value)
            update(result)
        }
    }

}

private class ArrayFunction<T>(
    private val sources: Array<Entity<*>>,
    private val method: (Array<Any?>) -> T
) : Function<T>() {

    private val context = collectContext(*sources)
    private val sourceValues: List<BehaviorSubject<Any?>> =
        sources.map { BehaviorSubject<Any?>() }

    init {
        sourceValues.forEachIndexed { index, subject ->
            sources[index].subscribe {
                subject.update(it)
                calculate()
            }
        }
    }

    override fun getContext(): Context {
        return context
    }

    private fun calculate() {
        val values: List<ValueHolder<Any?>?> = sourceValues.map { it.getValue() }
        if (values.all { it != null }) {
            val params: Array<Any?> = values.map { it!!.value }.toTypedArray()
            val result = method(params)
            update(result)
        }
    }

}