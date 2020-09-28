package net.apptronic.core.view

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.switchContext
import net.apptronic.core.view.base.CoreViewBase
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.container.ICoreContainerView
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.PropertyAccess
import net.apptronic.core.view.properties.VerticalAlignment
import net.apptronic.core.view.properties.Visibility

/**
 * Specifies base properties for multiplatform view.
 *
 * Used by target platforms to generate native views and layouts and update according to it's properties.
 */
interface ICoreView : CoreViewBase, PropertyAccess, ViewPropertyOwner {

    /**
     * [CoreViewBuilder] instance which allows to manually manipulate with creates views and not adds them anywhere.
     */
    val detachedViewBuilder: CoreViewBuilder

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

    val background: ViewProperty<ICoreView?>
    val foreground: ViewProperty<ICoreView?>

    /**
     * Apply all [style]s to this [ICoreView]
     */
    fun style(vararg style: CoreViewStyle)

    /**
     * Apply all [theme]s to this [ICoreView] and all children if it is [ICoreContainerView]
     */
    fun theme(vararg theme: CoreViewStyle)

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
        width.setDimension(size)
    }

    fun width(source: Entity<Number>) {
        width.setDimension(source)
    }

    fun height(size: Number) {
        height.setDimension(size)
    }

    fun height(source: Entity<Number>) {
        height.setDimension(source)
    }

    fun size(width: Number, height: Number) {
        width(width)
        height(height)
    }

    fun background(backgroundBuilder: CoreViewBuilder.() -> ICoreView) {
        background.set(detachedViewBuilder.backgroundBuilder())
    }

    fun foreground(backgroundBuilder: CoreViewBuilder.() -> ICoreView) {
        foreground.set(detachedViewBuilder.backgroundBuilder())
    }

    fun shadow(value: Number) {
        shadow.setDimension(value)
    }

    fun visibility(value: Visibility) {
        visibility.set(value)
    }

    fun visibility(source: Entity<Visibility>) {
        visibility.set(source)
    }

    fun visibleInvisible(source: Entity<Boolean>) {
        visibility.set(source.switchContext(context).map { if (it) Visible else Invisible })
    }

    fun visibleGone(source: Entity<Boolean>) {
        visibility.set(source.switchContext(context).map { if (it) Visible else Gone })
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
        indentStart.setDimension(value)
    }

    fun indentEnd(value: Number) {
        indentEnd.setDimension(value)
    }

    fun indentLeft(value: Number) {
        if (isLTR) {
            indentStart.setDimension(value)
        } else {
            indentEnd.setDimension(value)
        }
    }

    fun indentRight(value: Number) {
        if (isLTR) {
            indentEnd.setDimension(value)
        } else {
            indentStart.setDimension(value)
        }
    }

    fun indentTop(value: Number) {
        indentTop.setDimension(value)
    }

    fun indentBottom(value: Number) {
        indentBottom.setDimension(value)
    }

    fun indentStart(source: Entity<Number>) {
        indentStart.setDimension(source)
    }

    fun indentEnd(source: Entity<Number>) {
        indentEnd.setDimension(source)
    }

    fun indentLeft(source: Entity<Number>) {
        if (isLTR) {
            indentStart.setDimension(source)
        } else {
            indentEnd.setDimension(source)
        }
    }

    fun indentRight(source: Entity<Number>) {
        if (isLTR) {
            indentEnd.setDimension(source)
        } else {
            indentStart.setDimension(source)
        }
    }

    fun indentTop(source: Entity<Number>) {
        indentTop.setDimension(source)
    }

    fun indentBottom(source: Entity<Number>) {
        indentBottom.setDimension(source)
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
        paddingStart.setDimension(value)
    }

    fun paddingEnd(value: Number) {
        paddingEnd.setDimension(value)
    }

    fun paddingLeft(value: Number) {
        if (isLTR) {
            paddingStart.setDimension(value)
        } else {
            paddingEnd.setDimension(value)
        }
    }

    fun paddingRight(value: Number) {
        if (isLTR) {
            paddingEnd.setDimension(value)
        } else {
            paddingStart.setDimension(value)
        }
    }

    fun paddingTop(value: Number) {
        paddingTop.setDimension(value)
    }

    fun paddingBottom(value: Number) {
        paddingBottom.setDimension(value)
    }

    fun paddingStart(source: Entity<Number>) {
        paddingStart.setDimension(source)
    }

    fun paddingEnd(source: Entity<Number>) {
        paddingEnd.setDimension(source)
    }

    fun paddingLeft(source: Entity<Number>) {
        if (isLTR) {
            paddingStart.setDimension(source)
        } else {
            paddingEnd.setDimension(source)
        }
    }

    fun paddingRight(source: Entity<Number>) {
        if (isLTR) {
            paddingEnd.setDimension(source)
        } else {
            paddingStart.setDimension(source)
        }
    }

    fun paddingTop(source: Entity<Number>) {
        paddingTop.setDimension(source)
    }

    fun paddingBottom(source: Entity<Number>) {
        paddingBottom.setDimension(source)
    }

}

