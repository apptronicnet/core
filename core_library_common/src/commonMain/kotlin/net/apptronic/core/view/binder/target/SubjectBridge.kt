package net.apptronic.core.view.binder.target

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.base.observable.subject.Subject

/**
 * This class bridges [Subject] from [Target] to publish values
 */
class SubjectBridge<Target, E>(
        private val readMethod: Target.() -> Subject<E>,
) : Subject<E>, TargetBridge<Target> {

    private val subject = PublishSubject<E>()

    private var targetSubject: Subject<E>? = null

    override fun assignTarget(target: Target) {
        targetSubject = target.readMethod()
    }

    override fun releaseTarget() {
        targetSubject = null
    }

    override fun update(value: E) {
        subject.update(value)
    }

    override fun subscribe(observer: Observer<E>): Subscription {
        return subject.subscribe(observer)
    }

}