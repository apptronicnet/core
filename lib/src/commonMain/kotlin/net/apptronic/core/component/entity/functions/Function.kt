package net.apptronic.core.component.entity.functions

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.entity.collectContext
import net.apptronic.core.component.entity.subscriptions.ContextSubscriptionFactory
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.WorkerDefinition

abstract class Function<T> : EntityValue<T> {

    private val subject = BehaviorSubject<T>()
    private val observable = subject.distinctUntilChanged()
    protected abstract val functionContext: Context
    protected abstract var worker: Worker
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
    fun usingWorker(workerDefinition: WorkerDefinition): Function<T> {
        worker = getContext().getScheduler().getWorker(workerDefinition)
        return this
    }

    override fun getContext(): Context {
        return functionContext
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
    override val functionContext = sourceEntity.getContext()
    override var worker = functionContext.getScheduler().getWorker(WorkerDefinition.SYNCHRONOUS)
    override val subscriptions: ContextSubscriptionFactory<T> = ContextSubscriptionFactory(functionContext)

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

    private var leftValue = BehaviorSubject<A>()
    private var rightValue = BehaviorSubject<B>()
    override val functionContext = collectContext(left, right)
    override var worker = functionContext.getScheduler().getWorker(WorkerDefinition.SYNCHRONOUS)
    override val subscriptions: ContextSubscriptionFactory<T> = ContextSubscriptionFactory(functionContext)

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

    override val functionContext = collectContext(*sources)
    override var worker = functionContext.getScheduler().getWorker(WorkerDefinition.SYNCHRONOUS)
    override val subscriptions: ContextSubscriptionFactory<T> = ContextSubscriptionFactory(functionContext)
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