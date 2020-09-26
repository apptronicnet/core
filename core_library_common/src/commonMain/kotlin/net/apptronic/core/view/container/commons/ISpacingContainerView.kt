package net.apptronic.core.view.container.commons

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ViewProperty

/**
 * Base interface for view which can divide content with standard spacing
 */
interface ISpacingContainerView : CoreView {

    var spacing: ViewProperty<Number>

    fun spacing(value: Number) {
        spacing.set(value)
    }

    fun spacing(source: Observable<Number>) {
        spacing.set(source)
    }

}