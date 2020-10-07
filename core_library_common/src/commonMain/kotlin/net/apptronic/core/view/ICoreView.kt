package net.apptronic.core.view

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.switchContext
import net.apptronic.core.view.containers.ICoreContainerView
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
@UnderDevelopment
interface ICoreView : ICoreViewRepresentable, ViewPropertyOwner, PropertyAccess {

    var parent: ICoreParentView?

    val layoutAlignmentHorizontal: ViewProperty<HorizontalAlignment?>
    val layoutAlignmentVertical: ViewProperty<VerticalAlignment?>

    val width: ViewProperty<CoreLayoutDimension>
    val height: ViewProperty<CoreLayoutDimension>

    val paddingTop: ViewProperty<Number>
    val paddingBottom: ViewProperty<Number>
    val paddingLeft: ViewProperty<Number>
    val paddingRight: ViewProperty<Number>
    val paddingStart: ViewProperty<Number>
    val paddingEnd: ViewProperty<Number>
    val indentTop: ViewProperty<Number>
    val indentBottom: ViewProperty<Number>
    val indentLeft: ViewProperty<Number>
    val indentRight: ViewProperty<Number>
    val indentStart: ViewProperty<Number>
    val indentEnd: ViewProperty<Number>

    val shadow: ViewProperty<Number>
    val visibility: ViewProperty<Visibility>

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

    fun layoutAlignment(vertical: VerticalAlignment?) {
        layoutAlignmentVertical.setValue(vertical)
    }

    fun layoutAlignment(horizontal: HorizontalAlignment?) {
        layoutAlignmentHorizontal.setValue(horizontal)
    }

    fun layoutAlignment(horizontal: HorizontalAlignment?, vertical: VerticalAlignment?) {
        layoutAlignmentHorizontal.setValue(horizontal)
        layoutAlignmentVertical.setValue(vertical)
    }

    fun width(value: Number) {
        width.setValue(CoreLayoutDimension(value))
    }

    fun width(value: CoreLayoutDimension) {
        width.setValue(value)
    }

    fun height(value: Number) {
        height.setValue(CoreLayoutDimension(value))
    }

    fun height(value: CoreLayoutDimension) {
        height.setValue(value)
    }

    fun size(width: Number, height: Number) {
        width(width)
        height(height)
    }

    fun size(width: CoreLayoutDimension, height: CoreLayoutDimension) {
        width(width)
        height(height)
    }

    fun size(width: CoreLayoutDimension, height: Number) {
        width(width)
        height(height)
    }

    fun size(width: Number, height: CoreLayoutDimension) {
        width(width)
        height(height)
    }

    fun shadow(value: Number) {
        shadow.setValue(value)
    }

    fun visibility(value: Visibility) {
        visibility.setValue(value)
    }

    fun visibility(source: Entity<Visibility>) {
        visibility.setValue(source)
    }

    fun visibleInvisible(source: Entity<Boolean>) {
        visibility.setValue(source.switchContext(context).map { if (it) Visible else Invisible })
    }

    fun visibleGone(source: Entity<Boolean>) {
        visibility.setValue(source.switchContext(context).map { if (it) Visible else Gone })
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
        all?.let { indentTop(it); indentBottom(it); indentLeft(it); indentStart(it); indentRight(it); indentEnd(it) }
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
        indentStart.setValue(value)
    }

    fun indentEnd(value: Number) {
        indentEnd.setValue(value)
    }

    fun indentLeft(value: Number) {
        indentLeft.setValue(value)
    }

    fun indentRight(value: Number) {
        indentRight.setValue(value)
    }

    fun indentTop(value: Number) {
        indentTop.setValue(value)
    }

    fun indentBottom(value: Number) {
        indentBottom.setValue(value)
    }

    fun indentStart(source: Entity<Number>) {
        indentStart.setValue(source)
    }

    fun indentEnd(source: Entity<Number>) {
        indentEnd.setValue(source)
    }

    fun indentLeft(source: Entity<Number>) {
        indentLeft.setValue(source)
    }

    fun indentRight(source: Entity<Number>) {
        indentRight.setValue(source)
    }

    fun indentTop(source: Entity<Number>) {
        indentTop.setValue(source)
    }

    fun indentBottom(source: Entity<Number>) {
        indentBottom.setValue(source)
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
        all?.let { paddingTop(it); paddingBottom(it); paddingLeft(it); paddingStart(it); paddingRight(it); paddingEnd(it) }
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
        paddingStart.setValue(value)
    }

    fun paddingEnd(value: Number) {
        paddingEnd.setValue(value)
    }

    fun paddingLeft(value: Number) {
        paddingLeft.setValue(value)
    }

    fun paddingRight(value: Number) {
        paddingRight.setValue(value)
    }

    fun paddingTop(value: Number) {
        paddingTop.setValue(value)
    }

    fun paddingBottom(value: Number) {
        paddingBottom.setValue(value)
    }

    fun paddingStart(source: Entity<Number>) {
        paddingStart.setValue(source)
    }

    fun paddingEnd(source: Entity<Number>) {
        paddingEnd.setValue(source)
    }

    fun paddingLeft(source: Entity<Number>) {
        paddingLeft.setValue(source)
    }

    fun paddingRight(source: Entity<Number>) {
        paddingRight.setValue(source)
    }

    fun paddingTop(source: Entity<Number>) {
        paddingTop.setValue(source)
    }

    fun paddingBottom(source: Entity<Number>) {
        paddingBottom.setValue(source)
    }

}

