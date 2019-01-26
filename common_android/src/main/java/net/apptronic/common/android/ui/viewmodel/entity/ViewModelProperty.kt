package net.apptronic.common.android.ui.viewmodel.entity

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.apptronic.common.android.ui.viewmodel.entity.functions.Predicate
import net.apptronic.common.android.ui.viewmodel.entity.functions.subscribe
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class ViewModelProperty<T>(lifecycleHolder: LifecycleHolder) : ViewModelSubjectEntity<T>(
    lifecycleHolder,
    ValueEntitySubject(lifecycleHolder)
), Predicate<T> {

    fun set(value: T) {
        onSetValue(value)
        onInput(value)
    }

    abstract fun isSet(): Boolean

    protected abstract fun onSetValue(value: T)

    protected abstract fun onGetValue(): T

    override fun getPredicateSubjects(): Set<ViewModelProperty<*>> {
        return setOf(this)
    }

    override fun getPredicateValue(): T {
        return onGetValue()
    }

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

fun <E : ViewModelProperty<T>, T> E.setAs(predicate: Predicate<T>): E {
    predicate.subscribe {
        set(it)
    }
    return this
}

