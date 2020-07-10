package net.apptronic.core.features.service

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.extensions.BaseComponent

abstract class Service<T>(context: Context) : BaseComponent(context) {

    abstract suspend fun onNext(request: T)

}