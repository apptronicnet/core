package net.apptronic.common.android.ui.viewmodel.entity

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

open class ViewModelProperty<T>(lifecycleHolder: LifecycleHolder) : ViewModelSubjectEntity<T>(
    lifecycleHolder,
    ValueEntitySubject(lifecycleHolder)
) {

    internal var valueHolder: ValueHolder<T>? = null

    fun set(value: T) {
        this.valueHolder = ValueHolder(value)
        onInput(value)
    }

    fun set(property: ViewModelProperty<T>) {
        property.valueHolder?.let {
            set(it.value)
        }
    }

    fun get(): T {
        valueHolder?.let {
            return it.value
        } ?: throw PropertyNotSetException()
    }

    override fun asObservable(): Observable<T> {
        val result = BehaviorSubject.create<T>()
        subscribe {
            result.onNext(it)
        }
        return result
    }

    fun doIfSet(action: (T) -> Unit) {
        valueHolder?.let {
            action(it.value)
        }
    }

}

