package net.apptronic.core.view

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreDimensionZero
import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.dimension.FitToContentDimension
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment
import net.apptronic.core.view.properties.Visibility

abstract class CoreView(final override val context: CoreViewContext) : ICoreView {

    final override fun <T> viewProperty(initialValue: T): ViewProperty<T> {
        return super.viewProperty(initialValue)
    }

    private class CoreDetachedViewBuilder(override val context: CoreViewContext) : CoreViewBuilder

    final override val detachedViewBuilder: CoreViewBuilder = CoreDetachedViewBuilder(context)

    override val layoutAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)
    override val layoutAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

    override val width = viewProperty<CoreLayoutDimension>(FitToContentDimension)
    override val height = viewProperty<CoreLayoutDimension>(FitToContentDimension)

    override val background = viewProperty<ICoreView?>(null)
    override val foreground = viewProperty<ICoreView?>(null)

    override val paddingTop = viewProperty<CoreDimension>(CoreDimensionZero)
    override val paddingBottom = viewProperty<CoreDimension>(CoreDimensionZero)
    override val paddingStart = viewProperty<CoreDimension>(CoreDimensionZero)
    override val paddingEnd = viewProperty<CoreDimension>(CoreDimensionZero)
    override val indentTop = viewProperty<CoreDimension>(CoreDimensionZero)
    override val indentBottom = viewProperty<CoreDimension>(CoreDimensionZero)
    override val indentStart = viewProperty<CoreDimension>(CoreDimensionZero)
    override val indentEnd = viewProperty<CoreDimension>(CoreDimensionZero)
    override val shadow = viewProperty<CoreDimension>(CoreDimensionZero)
    override val visibility = viewProperty(Visibility.Visible)

    final override fun style(vararg style: CoreViewStyle) {
        style.forEach {
            it.applyTo(this)
        }
    }

    final override fun theme(vararg theme: CoreViewStyle) {
        theme.forEach {
            context.theme(this, it)
        }
    }


}

