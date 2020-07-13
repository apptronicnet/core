package net.apptronic.core.features.service

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.extensions.BaseComponent

abstract class Service<T : Any, R : Any>(context: Context) : BaseComponent(context) {

    abstract suspend fun onNext(request: T): R

    open fun onError(request: T, e: Exception) {
        throw e
    }

}