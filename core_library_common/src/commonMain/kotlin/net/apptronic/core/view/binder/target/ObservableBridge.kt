package net.apptronic.core.view.binder.target

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.base.observable.subscribe

/**
 * This class bridges data from [Target] which can be changed dynamically.
 */
class ObservableBridge<Target, E, T>(
        private val readMethod: Target.() -> Observable<E>,
        private val readValueFunction: (E) -> T
) : TargetBridge<Target>, Observable<T> {

    private val subject = PublishSubject<T>()

    private var currentSubscription: Subscription? = null

    override fun assignTarget(target: Target) {
        releaseTarget()
        val observable = target.readMethod()
        currentSubscription = observable.subscribe {
            val value = readValueFunction(it)
            subject.update(value)
        }
    }

    override fun releaseTarget() {
        currentSubscription?.unsubscribe()
        currentSubscription = null
    }

    override fun subscribe(observer: Observer<T>): Subscription {
        return subject.subscribe(observer)
    }

}