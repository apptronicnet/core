package net.apptronic.core.view

import net.apptronic.core.component.value
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

private class CoreDetachedViewBuilder(override val context: CoreViewContext) : CoreViewBuilder

abstract class CoreView(final override val context: CoreViewContext) : ICoreView {

    final override val detachedViewBuilder: CoreViewBuilder = CoreDetachedViewBuilder(context)

    override val layoutAlignmentHorizontal = value<HorizontalAlignment>(DefaultAlignment)
    override val layoutAlignmentVertical = value<VerticalAlignment>(DefaultAlignment)

    override val width = value<CoreLayoutDimension>(FitToContentDimension)
    override val height = value<CoreLayoutDimension>(FitToContentDimension)

    override val background = value<ICoreView?>(null)
    override val foreground = value<ICoreView?>(null)

    override val paddingTop = value<CoreDimension>(CoreDimensionZero)
    override val paddingBottom = value<CoreDimension>(CoreDimensionZero)
    override val paddingStart = value<CoreDimension>(CoreDimensionZero)
    override val paddingEnd = value<CoreDimension>(CoreDimensionZero)
    override val indentTop = value<CoreDimension>(CoreDimensionZero)
    override val indentBottom = value<CoreDimension>(CoreDimensionZero)
    override val indentStart = value<CoreDimension>(CoreDimensionZero)
    override val indentEnd = value<CoreDimension>(CoreDimensionZero)
    override val shadow = value<CoreDimension>(CoreDimensionZero)
    override val visibility = value(Visibility.Visible)

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

