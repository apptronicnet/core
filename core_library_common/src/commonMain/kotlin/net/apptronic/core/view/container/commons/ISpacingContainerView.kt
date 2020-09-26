package net.apptronic.core.view.container.commons

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.setAsCoreDimension

/**
 * Base interface for view which can divide content with standard spacing
 */
interface ISpacingContainerView : CoreView {

    val spacing: ViewProperty<CoreDimension>

    fun spacing(value: Number) {
        spacing.setAsCoreDimension(value)
    }

    fun spacing(source: Observable<Number>) {
        spacing.setAsCoreDimension(source)
    }

}