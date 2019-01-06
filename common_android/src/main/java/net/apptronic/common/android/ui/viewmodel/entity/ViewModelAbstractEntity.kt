package net.apptronic.common.android.ui.viewmodel.entity

import io.reactivex.Observable
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

abstract class ViewModelAbstractEntity<T>(private val lifecycleHolder: LifecycleHolder<*>) {

    protected abstract fun onInput(value: T)

    protected abstract fun onListen(listener: (T) -> Unit, stage: LifecycleStage)

    fun subscribe(listener: (T) -> Unit) {
        lifecycleHolder.localLifecycle().getActiveStage().let {
            onListen(listener, it)
        }
    }

    abstract fun asObservable(): Observable<T>

}