package net.apptronic.core.commons.service

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context

abstract class Service<T : Any, R : Any>(context: Context) : Component(context) {

    abstract suspend fun onNext(request: T): R

    open fun onError(request: T, e: Exception) {
        throw ServiceIsNotHandledErrorException("${this::class.simpleName} is not handled exception during request execution onNext(request: T): R\n" +
                "Please override ${this::class.simpleName}.onError(request: T, e: Exception) to handle exception.\n" +
                "If exception is handled by service invoker it is possible to ignore $e in overridden onError(request: T, e: Exception) method.", e)
    }

}