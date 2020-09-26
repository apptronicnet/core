package net.apptronic.core.view.container

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.base.CoreViewBase

interface SpacingContainerView : CoreViewBase {

    var spacing: ViewProperty<Number>

    fun spacing(value: Number) {
        spacing.set(value)
    }

    fun spacing(source: Observable<Number>) {
        spacing.set(source)
    }

}