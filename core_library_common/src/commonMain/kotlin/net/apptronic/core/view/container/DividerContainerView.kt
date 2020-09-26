package net.apptronic.core.view.container

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.CoreViewSet
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.base.CoreViewBase
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.layer.CoreViewLayer
import net.apptronic.core.view.properties.CoreColor
import net.apptronic.core.view.shape.CoreDrawable

interface DividerContainerView : CoreViewBase {

    var dividerHeight: ViewProperty<Number>

    var divider: CoreViewLayer?

    fun dividerHeight(value: Number) {
        dividerHeight.set(value)
    }

    fun dividerHeight(source: Observable<Number>) {
        dividerHeight.set(source)
    }

    fun divider(color: CoreColor) {
        divider = CoreViewLayer.Color(color)
    }

    fun divider(drawable: CoreDrawable) {
        divider = CoreViewLayer.Drawable(drawable)
    }

    fun divider(backgroundBuilder: CoreViewBuilder.() -> Unit) {
        val set = CoreViewSet(viewConfiguration)
        set.backgroundBuilder()
        divider = CoreViewLayer.ViewSet(set)
    }

}