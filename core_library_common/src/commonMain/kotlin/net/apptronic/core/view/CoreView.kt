package net.apptronic.core.view

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.base.CoreViewBase
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.container.commons.ICoreContainerView
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.dimension.setAsCoreDimension
import net.apptronic.core.view.dimension.setAsCoreLayoutDimension
import net.apptronic.core.view.layer.CoreViewLayer
import net.apptronic.core.view.properties.*
import net.apptronic.core.view.shape.CoreDrawable

/**
 * Specifies base properties for multiplatform view.
 *
 * Used by target platforms to generate native views and layouts and update according to it's properties.
 */
interface CoreView : CoreViewBase, PropertyAccess {

    /**
     * Creates mutable view property, which can be changed after view creation
     */
    fun <T> viewProperty(initialValue: T): ViewProperty<T>

    val layoutAlignmentHorizontal: ViewProperty<HorizontalAlignment>
    val layoutAlignmentVertical: ViewProperty<VerticalAlignment>

    val width: ViewProperty<CoreLayoutDimension>
    val height: ViewProperty<CoreLayoutDimension>

    val paddingTop: ViewProperty<CoreDimension>
    val paddingBottom: ViewProperty<CoreDimension>
    val paddingStart: ViewProperty<CoreDimension>
    val paddingEnd: ViewProperty<CoreDimension>
    val indentTop: ViewProperty<CoreDimension>
    val indentBottom: ViewProperty<CoreDimension>
    val indentStart: ViewProperty<CoreDimension>
    val indentEnd: ViewProperty<CoreDimension>

    val shadow: ViewProperty<CoreDimension>
    val visibility: ViewProperty<Visibility>

    val background: ViewProperty<CoreViewLayer?>
    val foreground: ViewProperty<CoreViewLayer?>

    /**
     * Apple all [style]s to this [CoreView]
     */
    fun style(vararg style: CoreViewStyle) {
        style.forEach {
            it.applyTo(this)
        }
    }

    /**
     * Apple all [style]s to this [CoreView] and all children if it is [ICoreContainerView]
     */
    fun theme(vararg theme: CoreViewStyle) {
        style(*theme)
    }

    fun theme(builder: CoreViewStyle.() -> Unit) {
        theme(viewTheme(builder))
    }

    fun layoutAlignment(vertical: VerticalAlignment) {
        layoutAlignmentVertical.set(vertical)
    }

    fun layoutAlignment(horizontal: HorizontalAlignment) {
        layoutAlignmentHorizontal.set(horizontal)
    }

    fun layoutAlignment(horizontal: HorizontalAlignment, vertical: VerticalAlignment) {
        layoutAlignmentHorizontal.set(horizontal)
        layoutAlignmentVertical.set(vertical)
    }

    fun width(size: Number) {
        width.setAsCoreLayoutDimension(size)
    }

    fun width(source: Observable<Number>) {
        width.setAsCoreLayoutDimension(source)
    }

    fun height(size: Number) {
        height.setAsCoreLayoutDimension(size)
    }

    fun height(source: Observable<Number>) {
        height.setAsCoreLayoutDimension(source)
    }

    fun size(width: Number, height: Number) {
        width(width)
        height(height)
    }

    fun background(color: CoreColor) {
        background.set(CoreViewLayer.Color(color))
    }

    fun background(drawable: CoreDrawable) {
        background.set(CoreViewLayer.Drawable(drawable))
    }

    fun background(backgroundBuilder: CoreViewBuilder.() -> Unit) {
        val set = CoreViewSet(viewConfiguration)
        set.backgroundBuilder()
        background.set(CoreViewLayer.ViewSet(set))
    }

    fun foreground(color: CoreColor) {
        background.set(CoreViewLayer.Color(color))
    }

    fun foreground(drawable: CoreDrawable) {
        background.set(CoreViewLayer.Drawable(drawable))
    }

    fun foreground(backgroundBuilder: CoreViewBuilder.() -> Unit) {
        val set = CoreViewSet(viewConfiguration)
        set.backgroundBuilder()
        background.set(CoreViewLayer.ViewSet(set))
    }

    fun shadow(value: Number) {
        shadow.setAsCoreDimension(value)
    }

    fun visibility(value: Visibility) {
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

    fun indent(
            all: Number? = null,
            left: Number? = null,
            right: Number? = null,
            top: Number? = null,
            bottom: Number? = null,
            start: Number? = null,
            end: Number? = null,
            horizontal: Number? = null,
            vertical: Number? = null,
    ) {
        all?.let { indentTop(it); indentBottom(it); indentStart(it); indentEnd(it) }
        left?.let { indentLeft(it) }
        right?.let { indentRight(it) }
        top?.let { indentTop(it) }
        bottom?.let { indentBottom(it) }
        start?.let { indentStart(it) }
        end?.let { indentEnd(it) }
        horizontal?.let { indentStart(it); indentEnd(it) }
        vertical?.let { indentTop(it); indentBottom(it) }
    }

    fun indentStart(value: Number) {
        indentStart.setAsCoreDimension(value)
    }

    fun indentEnd(value: Number) {
        indentEnd.setAsCoreDimension(value)
    }

    fun indentLeft(value: Number) {
        if (isLTR) {
            indentStart.setAsCoreDimension(value)
        } else {
            indentEnd.setAsCoreDimension(value)
        }
    }

    fun indentRight(value: Number) {
        if (isLTR) {
            indentEnd.setAsCoreDimension(value)
        } else {
            indentStart.setAsCoreDimension(value)
        }
    }

    fun indentTop(value: Number) {
        indentTop.setAsCoreDimension(value)
    }

    fun indentBottom(value: Number) {
        indentBottom.setAsCoreDimension(value)
    }

    fun indentStart(source: Observable<Number>) {
        indentStart.setAsCoreDimension(source)
    }

    fun indentEnd(source: Observable<Number>) {
        indentEnd.setAsCoreDimension(source)
    }

    fun indentLeft(source: Observable<Number>) {
        if (isLTR) {
            indentStart.setAsCoreDimension(source)
        } else {
            indentEnd.setAsCoreDimension(source)
        }
    }

    fun indentRight(source: Observable<Number>) {
        if (isLTR) {
            indentEnd.setAsCoreDimension(source)
        } else {
            indentStart.setAsCoreDimension(source)
        }
    }

    fun indentTop(source: Observable<Number>) {
        indentTop.setAsCoreDimension(source)
    }

    fun indentBottom(source: Observable<Number>) {
        indentBottom.setAsCoreDimension(source)
    }

    fun padding(
            all: Number? = null,
            left: Number? = null,
            right: Number? = null,
            top: Number? = null,
            bottom: Number? = null,
            start: Number? = null,
            end: Number? = null,
            horizontal: Number? = null,
            vertical: Number? = null,
    ) {
        all?.let { paddingTop(it); paddingBottom(it); paddingStart(it); paddingEnd(it) }
        left?.let { paddingLeft(it) }
        right?.let { paddingRight(it) }
        top?.let { paddingTop(it) }
        bottom?.let { paddingBottom(it) }
        start?.let { paddingStart(it) }
        end?.let { paddingEnd(it) }
        horizontal?.let { paddingStart(it); paddingEnd(it) }
        vertical?.let { paddingTop(it); paddingBottom(it) }
    }

    fun paddingStart(value: Number) {
        paddingStart.setAsCoreDimension(value)
    }

    fun paddingEnd(value: Number) {
        paddingEnd.setAsCoreDimension(value)
    }

    fun paddingLeft(value: Number) {
        if (isLTR) {
            paddingStart.setAsCoreDimension(value)
        } else {
            paddingEnd.setAsCoreDimension(value)
        }
    }

    fun paddingRight(value: Number) {
        if (isLTR) {
            paddingEnd.setAsCoreDimension(value)
        } else {
            paddingStart.setAsCoreDimension(value)
        }
    }

    fun paddingTop(value: Number) {
        paddingTop.setAsCoreDimension(value)
    }

    fun paddingBottom(value: Number) {
        paddingBottom.setAsCoreDimension(value)
    }

    fun paddingStart(source: Observable<Number>) {
        paddingStart.setAsCoreDimension(source)
    }

    fun paddingEnd(source: Observable<Number>) {
        paddingEnd.setAsCoreDimension(source)
    }

    fun paddingLeft(source: Observable<Number>) {
        if (isLTR) {
            paddingStart.setAsCoreDimension(source)
        } else {
            paddingEnd.setAsCoreDimension(source)
        }
    }

    fun paddingRight(source: Observable<Number>) {
        if (isLTR) {
            paddingEnd.setAsCoreDimension(source)
        } else {
            paddingStart.setAsCoreDimension(source)
        }
    }

    fun paddingTop(source: Observable<Number>) {
        paddingTop.setAsCoreDimension(source)
    }

    fun paddingBottom(source: Observable<Number>) {
        paddingBottom.setAsCoreDimension(source)
    }

}

