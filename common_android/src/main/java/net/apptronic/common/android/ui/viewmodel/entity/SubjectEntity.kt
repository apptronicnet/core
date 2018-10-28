package net.apptronic.common.android.ui.viewmodel.entity

import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleEvent
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

open class SubjectEntity<T>(lifecycleHolder: LifecycleHolder<*>,
                            internal val subject: ViewModelSubject<T>) : ViewModelEntity<T>(lifecycleHolder) {

    override fun onInput(value: T) {
        subject.send(value)
    }

    override fun onListen(listener: (T) -> Unit, stage: LifecycleStage) {
        val subscription = subject.subscribe {
            listener(it)
        }
        stage.subscribeExit(object : LifecycleEvent.Listener {
            override fun onEvent(event: LifecycleEvent) {
                subscription.unsubscribe()
                event.unsubscribe(this)
            }
        })
    }

    protected fun subject() = subject

}