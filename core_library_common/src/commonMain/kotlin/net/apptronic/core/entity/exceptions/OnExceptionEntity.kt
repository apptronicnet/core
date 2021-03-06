package net.apptronic.core.entity.exceptions

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.entity.BaseEntity
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription

internal class OnExceptionEntity<T>(
        val source: Entity<TryCatchResult<T>>,
        val exceptionHandler: (Exception) -> Unit
) : BaseEntity<T>() {

    private class OnlySuccessObserver<T>(
            private val target: Observer<T>
    ) : Observer<TryCatchResult<T>> {

        override fun update(value: TryCatchResult<T>) {
            if (value is TryCatchResult.Success) {
                target.update(value.result)
            }
        }

    }

    override val context: Context = source.context

    init {
        source.subscribe(context) {
            if (it is TryCatchResult.Failure) {
                exceptionHandler(it.exception)
            }
        }
    }

    override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<T>): EntitySubscription {
        return source.subscribe(targetContext, OnlySuccessObserver(targetObserver))
    }

}