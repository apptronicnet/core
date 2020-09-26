package net.apptronic.core.view.widgets.commons

import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.view.CoreContentView

interface ICoreButtonView : CoreContentView, IEnabledDisabledView {

    var onClick: () -> Any

    var onLongClick: () -> Any

    fun onClick(action: () -> Any) {
        onClick = action
    }

    fun onClick(subject: Subject<Unit>) {
        onClick = {
            subject.update(Unit)
        }
    }

    fun <T> onClick(subject: Subject<T>, value: T) {
        onClick = {
            subject.update(value)
        }
    }

    fun onLongClick(action: () -> Any) {
        onLongClick = action
    }

    fun onLongClick(subject: Subject<Unit>) {
        onLongClick = {
            subject.update(Unit)
        }
    }

    fun <T> onLongClick(subject: Subject<T>, value: T) {
        onLongClick = {
            subject.update(value)
        }
    }


}