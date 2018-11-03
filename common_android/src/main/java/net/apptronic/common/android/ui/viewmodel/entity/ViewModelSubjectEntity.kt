package net.apptronic.common.android.ui.viewmodel.entity

import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

abstract class ViewModelSubjectEntity<T>(lifecycleHolder: LifecycleHolder<*>,
                                         internal val entitySubject: ViewModelEntitySubject<T>) : ViewModelAbstractEntity<T>(lifecycleHolder) {

    override fun onInput(value: T) {
        entitySubject.send(value)
    }

    override fun onListen(listener: (T) -> Unit, stage: LifecycleStage) {
        val subscription = entitySubject.subscribe {
            listener(it)
        }
        stage.doOnExit {
            subscription.unsubscribe()
        }
    }

    protected fun subject() = entitySubject

}