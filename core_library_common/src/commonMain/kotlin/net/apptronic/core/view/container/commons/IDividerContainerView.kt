package net.apptronic.core.view.container.commons

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.CoreViewSet
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.setAsCoreDimension
import net.apptronic.core.view.layer.CoreViewLayer
import net.apptronic.core.view.properties.CoreColor
import net.apptronic.core.view.shape.CoreDrawable

/**
 * Base interface for view which can divide content with dividers
 */
interface IDividerContainerView : CoreView {

    val dividerSize: ViewProperty<CoreDimension>

    val divider: ViewProperty<CoreViewLayer?>

    val dividerOnTop: ViewProperty<Boolean>

    val dividerOnBottom: ViewProperty<Boolean>

    fun dividerSize(value: Number) {
        dividerSize.setAsCoreDimension(value)
    }

    fun dividerSize(source: Observable<Number>) {
        dividerSize.setAsCoreDimension(source)
    }

    fun divider(color: CoreColor) {
        divider.set(CoreViewLayer.Color(color))
    }

    fun divider(drawable: CoreDrawable) {
        divider.set(CoreViewLayer.Drawable(drawable))
    }

    fun dividerOnTop(value: Boolean) {
        dividerOnTop.set(value)
    }

    fun dividerOnTop(source: Observable<Boolean>) {
        dividerOnTop.set(source)
    }

    fun dividerOnBottom(value: Boolean) {
        dividerOnBottom.set(value)
    }

    fun dividerOnBottom(source: Observable<Boolean>) {
        dividerOnBottom.set(source)
    }

    fun divider(backgroundBuilder: CoreViewBuilder.() -> Unit) {
        val set = CoreViewSet(viewConfiguration)
        set.backgroundBuilder()
        divider.set(CoreViewLayer.ViewSet(set))
    }

}