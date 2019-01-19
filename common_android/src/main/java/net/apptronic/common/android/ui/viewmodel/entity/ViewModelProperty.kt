package net.apptronic.common.android.ui.viewmodel.entity

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class ViewModelProperty<T>(lifecycleHolder: LifecycleHolder) : ViewModelSubjectEntity<T>(
    lifecycleHolder,
    ValueEntitySubject(lifecycleHolder)
) {

    fun set(value: T) {
        onSetValue(value)
        onInput(value)
    }

    abstract fun isSet(): Boolean

    protected abstract fun onSetValue(value: T)

    protected abstract fun onGetValue(): T

    fun set(property: ViewModelProperty<T>): Boolean {
        return try {
            set(property.get())
            true
        } catch (e: PropertyNotSetException) {
            false
        }
    }

    fun get(): T {
        return onGetValue()
    }

    fun getOrNull(): T? {
        return try {
            get()
        } catch (e: PropertyNotSetException) {
            null
        }
    }

    override fun asObservable(): Observable<T> {
        val result = BehaviorSubject.create<T>()
        subscribe {
            result.onNext(it)
        }
        return result
    }

    fun doIfSet(action: (T) -> Unit): Boolean {
        return try {
            action(get())
            true
        } catch (e: PropertyNotSetException) {
            false
        }
    }

}

