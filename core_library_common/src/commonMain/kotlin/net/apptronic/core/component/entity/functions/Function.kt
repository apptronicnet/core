package net.apptronic.core.component.entity.functions

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.entity.collectContext
import net.apptronic.core.component.entity.subscriptions.ContextSubscriptionFactory

abstract class Function<T> : EntityValue<T> {

    private val subject = BehaviorSubject<T>()
    private val observable = subject
    protected var coroutineDispatcher: CoroutineDispatcher? = null
    protected abstract val subscriptions: ContextSubscriptionFactory<T>

    override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

    internal fun update(value: T) {
        subject.update(value)
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subscriptions.using(context).subscribe(observer, observable)
    }

    /**
     * Set worker to be user for calculation of function and notificatitons to observers. In case
     * if worker is not set  function calculation and update will be called synchronously
     * by source value.
     */
    fun withDispatcher(coroutineDispatcher: CoroutineDispatcher): Function<T> {
        this.coroutineDispatcher = coroutineDispatcher
        return this
    }

    @Deprecated("Should use coroutines")
    fun usingWorker(coroutineDispatcher: CoroutineDispatcher): Function<T> {
        this.coroutineDispatcher = coroutineDispatcher
        return this
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

    override val context = sourceEntity.context
    private var sourceSubject = BehaviorSubject<X>()
    override val subscriptions: ContextSubscriptionFactory<T> = ContextSubscriptionFactory(context)

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


private class DoubleFunction<T, A, B>(
    left: Entity<A>,
    right: Entity<B>,
    private val method: (A, B) -> T
) : Function<T>() {

    override val context = collectContext(left, right)
    private var leftValue = BehaviorSubject<A>()
    private var rightValue = BehaviorSubject<B>()
    override val subscriptions: ContextSubscriptionFactory<T> = ContextSubscriptionFactory(context)

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

    override val context = collectContext(*sources)
    override val subscriptions: ContextSubscriptionFactory<T> = ContextSubscriptionFactory(context)
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

    private fun calculate() {
        val values: List<ValueHolder<Any?>?> = sourceValues.map { it.getValue() }
        if (values.all { it != null }) {
            val params: Array<Any?> = values.map { it!!.value }.toTypedArray()
            val result = method(params)
            update(result)
        }
    }

}