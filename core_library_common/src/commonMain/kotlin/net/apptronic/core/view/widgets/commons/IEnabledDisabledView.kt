package net.apptronic.core.view.widgets.commons

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ViewProperty

interface IEnabledDisabledView : CoreView {

    var isEnabled: ViewProperty<Boolean>

    fun enabled(value: Boolean) {
        isEnabled.set(value)
    }

    fun enabled(source: Observable<Boolean>) {
        isEnabled.set(source)
    }

}