package net.apptronic.core.view.binder.target

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.subject.BehaviorSubject

/**
 * This class bridges value from [Target] which can be changed dynamically.
 */
class ValueBridge<Target, E, T>(
        private val readMethod: Target.() -> E,
        private val readValueFunction: (E) -> T
) : Observable<T>, TargetBridge<Target> {

    private val subject = BehaviorSubject<T>()

    override fun assignTarget(target: Target) {
        val sourceValue = target.readMethod()
        val resultValue = readValueFunction(sourceValue)
        subject.update(resultValue)
    }

    override fun releaseTarget() {
        // unused
    }

    override fun subscribe(observer: Observer<T>): Subscription {
        return subject.subscribe(observer)
    }

}