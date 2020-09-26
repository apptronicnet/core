package net.apptronic.core.view

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.base.CoreViewBase
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.dimension.exactLayoutDimension
import net.apptronic.core.view.layer.CoreViewLayer
import net.apptronic.core.view.properties.*
import net.apptronic.core.view.shape.CoreDrawable

interface CoreView : CoreViewBase, PropertyAccess {

    fun <T> viewProperty(initialValue: T): ViewProperty<T>

    override fun recycle() {
        background?.recycle()
        foreground?.recycle()
    }

    var layoutAlignmentHorizontal: HorizontalAlignment
    var layoutAlignmentVertical: VerticalAlignment

    var width: CoreLayoutDimension
    var height: CoreLayoutDimension

    val paddingTop: ViewProperty<Number>
    val paddingBottom: ViewProperty<Number>
    val paddingStart: ViewProperty<Number>
    val paddingEnd: ViewProperty<Number>
    val indentTop: ViewProperty<Number>
    val indentBottom: ViewProperty<Number>
    val indentStart: ViewProperty<Number>
    val indentEnd: ViewProperty<Number>
    val shadow: ViewProperty<Number>
    val visibility: ViewProperty<Visibility>

    fun layoutAlignment(vertical: VerticalAlignment) {
        layoutAlignmentVertical = vertical
    }

    fun layoutAlignment(horizontal: HorizontalAlignment) {
        layoutAlignmentHorizontal = horizontal
    }

    fun layoutAlignment(horizontal: HorizontalAlignment, vertical: VerticalAlignment) {
        layoutAlignmentHorizontal = horizontal
        layoutAlignmentVertical = vertical
    }

    var background: CoreViewLayer?
    var foreground: CoreViewLayer?

    fun width(dimension: CoreLayoutDimension) {
        width = dimension
    }

    fun width(size: Number) {
        width = exactLayoutDimension(size)
    }

    fun height(dimension: CoreLayoutDimension) {
        height = dimension
    }

    fun height(size: Number) {
        height = exactLayoutDimension(size)
    }

    fun background(color: CoreColor) {
        background = CoreViewLayer.Color(color)
    }

    fun background(drawable: CoreDrawable) {
        background = CoreViewLayer.Drawable(drawable)
    }

    fun background(backgroundBuilder: CoreViewBuilder.() -> Unit) {
        val set = CoreViewSet(viewConfiguration)
        set.backgroundBuilder()
        background = CoreViewLayer.ViewSet(set)
    }

    fun foreground(color: CoreColor) {
        background = CoreViewLayer.Color(color)
    }

    fun foreground(drawable: CoreDrawable) {
        background = CoreViewLayer.Drawable(drawable)
    }

    fun foreground(backgroundBuilder: CoreViewBuilder.() -> Unit) {
        val set = CoreViewSet(viewConfiguration)
        set.backgroundBuilder()
        background = CoreViewLayer.ViewSet(set)
    }

    fun shadow(value: Number) {
        shadow.set(value)
    }

    fun visiblity(value: Visibility) {
        visibility.set(value)
    }

    fun visibility(source: Observable<Visibility>) {
        visibility.set(source)
    }

    fun visibleInvisible(source: Observable<Boolean>) {
        visibility.set(source) {
            if (it) Visible else Invisible
        }
    }

    fun visibleGone(source: Observable<Boolean>) {
        visibility.set(source) {
            if (it) Visible else Gone
        }
    }

}

