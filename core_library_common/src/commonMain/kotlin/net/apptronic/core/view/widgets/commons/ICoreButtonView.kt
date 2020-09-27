package net.apptronic.core.view.widgets.commons

import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.view.CoreContentView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.binder.SupportsTargetBridge

interface ICoreButtonView : CoreContentView, IEnabledDisabledView {

    val onClick: ViewProperty<() -> Unit>

    val onLongClick: ViewProperty<() -> Unit>

    fun onClick(action: () -> Unit) {
        onClick.set(action)
    }

    fun <T> SupportsTargetBridge<T>.onClick(bridger: T.() -> Unit) {
        onClick(writeInvocation(bridger))
    }

    fun onClick(subject: Subject<Unit>) {
        onClick.set {
            subject.update(Unit)
        }
    }

    fun <T> SupportsTargetBridge<T>.onClickTo(bridger: T.() -> Subject<Unit>) {
        onClick(writeSubject(bridger))
    }

    fun <T> onClick(subject: Subject<T>, value: T) {
        onClick.set {
            subject.update(value)
        }
    }

    fun onLongClick(action: () -> Unit) {
        onLongClick.set(action)
    }

    fun <T> SupportsTargetBridge<T>.onLongClick(bridger: T.() -> Unit) {
        onLongClick(writeInvocation(bridger))
    }

    fun onLongClick(subject: Subject<Unit>) {
        onLongClick.set {
            subject.update(Unit)
        }
    }

    fun <T> onLongClick(subject: Subject<T>, value: T) {
        onLongClick.set {
            subject.update(value)
        }
    }

    fun <T> SupportsTargetBridge<T>.onLongClickTo(bridger: T.() -> Subject<Unit>) {
        onLongClick(writeSubject(bridger))
    }

}