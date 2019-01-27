package net.apptronic.common.core.mvvm.viewmodel.entity

import io.reactivex.Observable
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.Lifecycle
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.LifecycleStage

abstract class ViewModelAbstractEntity<T>(internal val lifecycleHolder: LifecycleHolder) {

    internal var id: String? = null

    internal abstract fun onInput(value: T)

    internal abstract fun onListen(listener: (T) -> Unit, stage: LifecycleStage)

    fun subscribe(listener: (T) -> Unit) {
        lifecycleHolder.getLifecycle().getActiveStage().let {
            onListen(listener, it)
        }
    }

    abstract fun asObservable(): Observable<T>

    override fun toString(): String {
        return javaClass.simpleName + if (id != null) ":id=$id" else ""
    }

}

fun <E : ViewModelAbstractEntity<T>, T> E.withId(id: String): E {
    this.id = id
    return this
}

fun <E : ViewModelAbstractEntity<T>, T> E.setup(setupBlock: E.() -> Unit): E {
    lifecycleHolder.getLifecycle().getStage(Lifecycle.ROOT_STAGE)?.doOnEnter {
        this@setup.setupBlock()
    }
    return this
}