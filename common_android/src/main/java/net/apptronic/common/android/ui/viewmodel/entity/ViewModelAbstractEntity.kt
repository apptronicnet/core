package net.apptronic.common.android.ui.viewmodel.entity

import io.reactivex.Observable
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

abstract class ViewModelAbstractEntity<T>(internal val lifecycleHolder: LifecycleHolder) {

    protected abstract fun onInput(value: T)

    protected abstract fun onListen(listener: (T) -> Unit, stage: LifecycleStage)

    fun subscribe(listener: (T) -> Unit) {
        lifecycleHolder.getLifecycle().getActiveStage().let {
            onListen(listener, it)
        }
    }

    abstract fun asObservable(): Observable<T>

}

fun <E : ViewModelAbstractEntity<T>, T> E.setup(setupBlock: E.() -> Unit): E {
    lifecycleHolder.getLifecycle().getStage(Lifecycle.ROOT_STAGE)?.doOnEnter {
        this@setup.setupBlock()
    }
    return this
}

