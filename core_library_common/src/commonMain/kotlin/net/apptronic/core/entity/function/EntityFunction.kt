package net.apptronic.core.entity.function

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.subject.BehaviorSubject
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.context.coroutines.serialThrottler
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.EntityValue
import net.apptronic.core.entity.base.ObservableEntity
import net.apptronic.core.entity.collectContext

interface FunctionAction<T, R> {

    fun execute(input: T, resultCallback: (R) -> Unit)

}

private class SynchronousFunctionAction<T, R>(
        private val calculation: (T) -> R
) : FunctionAction<T, R> {

    override fun execute(input: T, resultCallback: (R) -> Unit) {
        val result = calculation.invoke(input)
        resultCallback(result)
    }

}

private fun <T, R> syncAction(calculation: (T) -> R): FunctionAction<T, R> {
    return SynchronousFunctionAction(calculation)
}

private class CoroutineFunctionAction<T, R>(
        context: Context,
        private val calculation: suspend CoroutineScope.(T) -> R
) : FunctionAction<T, R> {

    private val coroutineThrottler = context.lifecycleCoroutineScope.serialThrottler()

    override fun execute(input: T, resultCallback: (R) -> Unit) {
        coroutineThrottler.launch {
            val result = this.calculation(input)
            resultCallback(result)
        }
    }

}

private fun <T, R> suspendAction(context: Context, calculation: suspend CoroutineScope.(T) -> R): FunctionAction<T, R> {
    return CoroutineFunctionAction(context, calculation)
}

abstract class EntityFunction<T>(
        override val context: Context
) : ObservableEntity<T>(), EntityValue<T> {

    override val observable = BehaviorSubject<T>()

    override fun getValueHolder(): ValueHolder<T>? {
        return observable.getValue()
    }

    internal fun update(value: T) {
        observable.update(value)
    }

}

fun <T, A> entityFunction(
        source: Entity<A>,
        method: (A) -> T
): EntityFunction<T> {
    val context = source.context
    val functionAction = syncAction(method)
    return SingleFunction(context, source, functionAction)
}

fun <T, A> entityFunctionSuspend(
        source: Entity<A>,
        method: suspend CoroutineScope.(A) -> T
): EntityFunction<T> {
    val context = source.context
    val functionAction = suspendAction(context, method)
    return SingleFunction(context, source, functionAction)
}

fun <T> entityArrayFunction(
        source: Array<out Entity<*>>,
        method: (Array<Any?>) -> T
): EntityFunction<T> {
    val context = collectContext(*source)
    val functionAction = syncAction(method)
    return ArrayFunction(context, source, functionAction)
}

fun <T> entityArrayFunctionSuspend(
        source: Array<Entity<*>>,
        method: suspend CoroutineScope.(Array<Any?>) -> T
): EntityFunction<T> {
    val context = collectContext(*source)
    val functionAction = suspendAction(context, method)
    return ArrayFunction(context, source, functionAction)
}

fun <T, A, B> entityFunction(
        left: Entity<A>,
        right: Entity<B>,
        method: (A, B) -> T
): EntityFunction<T> {
    val context = collectContext(left, right)
    val functionAction = syncAction<Pair<A, B>, T> {
        method(it.first, it.second)
    }
    return DoubleFunction(context, left, right, functionAction)
}

fun <T, A, B> entityFunctionSuspend(
        left: Entity<A>,
        right: Entity<B>,
        method: suspend CoroutineScope.(A, B) -> T
): EntityFunction<T> {
    val context = collectContext(left, right)
    val functionAction = suspendAction<Pair<A, B>, T>(context) {
        method(it.first, it.second)
    }
    return DoubleFunction(context, left, right, functionAction)
}

@Suppress("UNCHECKED_CAST")
fun <T, A, B, C> entityFunction(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        method: (A, B, C) -> T
): EntityFunction<T> {
    val context = collectContext(a, b, c)
    val functionAction = syncAction<Array<Any?>, T> {
        method(it[0] as A, it[1] as B, it[2] as C)
    }
    return ArrayFunction(context, arrayOf(a, b, c), functionAction)
}

@Suppress("UNCHECKED_CAST")
fun <T, A, B, C> entityFunctionSuspend(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        method: suspend CoroutineScope.(A, B, C) -> T
): EntityFunction<T> {
    val context = collectContext(a, b, c)
    val functionAction = suspendAction<Array<Any?>, T>(context) {
        method(it[0] as A, it[1] as B, it[2] as C)
    }
    return ArrayFunction(context, arrayOf(a, b, c), functionAction)
}

@Suppress("UNCHECKED_CAST")
fun <T, A, B, C, D> entityFunction(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        method: (A, B, C, D) -> T
): EntityFunction<T> {
    val context = collectContext(a, b, c, d)
    val functionAction = syncAction<Array<Any?>, T> {
        method(it[0] as A, it[1] as B, it[2] as C, it[3] as D)
    }
    return ArrayFunction(context, arrayOf(a, b, c, d), functionAction)
}

@Suppress("UNCHECKED_CAST")
fun <T, A, B, C, D> entityFunctionSuspend(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        method: suspend CoroutineScope.(A, B, C, D) -> T
): EntityFunction<T> {
    val context = collectContext(a, b, c, d)
    val functionAction = suspendAction<Array<Any?>, T>(context) {
        method(it[0] as A, it[1] as B, it[2] as C, it[3] as D)
    }
    return ArrayFunction(context, arrayOf(a, b, c, d), functionAction)
}

@Suppress("UNCHECKED_CAST")
fun <T, A, B, C, D, E> entityFunction(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>,
        method: (A, B, C, D, E) -> T
): EntityFunction<T> {
    val context = collectContext(a, b, c, d, e)
    val functionAction = syncAction<Array<Any?>, T> {
        method(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E)
    }
    return ArrayFunction(context, arrayOf(a, b, c, d, e), functionAction)
}

@Suppress("UNCHECKED_CAST")
fun <T, A, B, C, D, E> entityFunctionSuspend(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>,
        method: suspend CoroutineScope.(A, B, C, D, E) -> T
): EntityFunction<T> {
    val context = collectContext(a, b, c, d, e)
    val functionAction = suspendAction<Array<Any?>, T>(context) {
        method(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E)
    }
    return ArrayFunction(context, arrayOf(a, b, c, d, e), functionAction)
}

private class SingleFunction<T, X>(
        context: Context,
        private val sourceEntity: Entity<X>,
        private val functionAction: FunctionAction<X, T>
) : EntityFunction<T>(context) {

    private var sourceSubject = BehaviorSubject<X>()

    init {
        sourceEntity.subscribe {
            sourceSubject.update(it)
            calculate()
        }
    }

    private fun calculate() {
        val source = sourceSubject.getValue()
        if (source != null) {
            functionAction.execute(source.value, ::update)
        }
    }

}

private class DoubleFunction<T, A, B>(
        context: Context,
        private val left: Entity<A>,
        private val right: Entity<B>,
        private val functionAction: FunctionAction<Pair<A, B>, T>
) : EntityFunction<T>(context) {

    private var leftValue = BehaviorSubject<A>()
    private var rightValue = BehaviorSubject<B>()

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
            functionAction.execute(left.value to right.value, ::update)
        }
    }

}

private class ArrayFunction<T>(
        context: Context,
        private val sources: Array<out Entity<*>>,
        private val functionAction: FunctionAction<Array<Any?>, T>
) : EntityFunction<T>(context) {

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
            functionAction.execute(params, ::update)
        }
    }

}