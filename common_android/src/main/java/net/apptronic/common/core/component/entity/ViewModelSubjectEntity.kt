package net.apptronic.common.core.component.entity

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.lifecycle.LifecycleStageImpl

abstract class ViewModelSubjectEntity<T>(
    context: ComponentContext,
    private val entitySubject: ViewModelEntitySubject<T>
) : ViewModelAbstractEntity<T>(lifecycleHolder) {

    override fun onInput(value: T) {
        entitySubject.send(value)
    }

    override fun onListen(listener: (T) -> Unit, stage: LifecycleStageImpl) {
        val subscription = entitySubject.subscribe {
            listener(it)
        }
        stage.doOnExit {
            subscription.unsubscribe()
        }
    }

    protected fun subject() = entitySubject

}