package net.apptronic.core.view

import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.dimension.*
import net.apptronic.core.view.properties.*

abstract class CoreView(final override val context: CoreViewContext) : ICoreView {

    final override val FitToParent = CoreLayoutSpec.FitToParent
    final override val FitToContent = CoreLayoutSpec.FitToContent

    final override val Visible: Visibility = Visibility.Visible
    final override val Invisible: Visibility = Visibility.Invisible
    final override val Gone: Visibility = Visibility.Gone

    final override val Number.pixels: CoreDimension
        get() = DiscreteCoreDimension(this.toFloat())
    final override val Number.dimension: CoreDimension
        get() = PixelCoreDimension(this.toFloat())
    final override val CoreDimensionZero: CoreDimension = DiscreteCoreDimension(0f)

    final override val ToLeft: HorizontalAlignment = LayoutAlignment.ToLeft
    final override val ToStart: HorizontalAlignment = LayoutAlignment.ToStart
    final override val ToRight: HorizontalAlignment = LayoutAlignment.ToRight
    final override val ToEnd: HorizontalAlignment = LayoutAlignment.ToEnd
    final override val ToTop: VerticalAlignment = LayoutAlignment.ToTop
    final override val ToBottom: VerticalAlignment = LayoutAlignment.ToBottom
    final override val ToCenter: BidirectionalLayoutAlignment = LayoutAlignment.ToCenter
    final override val ToCenterVertical: VerticalAlignment = LayoutAlignment.ToCenterVertical
    final override val ToCenterHorizontal: HorizontalAlignment = LayoutAlignment.ToCenterHorizontal
    final override val DefaultAlignment: BidirectionalLayoutAlignment = LayoutAlignment.DefaultAlignment

    final override fun <T> viewProperty(initialValue: T, onRecycle: ((T) -> Unit)?): ViewProperty<T> {
        return super.viewProperty(initialValue, onRecycle)
    }

    private class CoreDetachedViewBuilder(override val context: CoreViewContext) : CoreViewBuilder

    final override val detachedViewBuilder: CoreViewBuilder = CoreDetachedViewBuilder(context)

    override val layoutAlignmentHorizontal = viewProperty<HorizontalAlignment?>(null)
    override val layoutAlignmentVertical = viewProperty<VerticalAlignment?>(null)

    override val width = viewProperty<CoreLayoutDimension>(FitToContent)
    override val height = viewProperty<CoreLayoutDimension>(FitToContent)

    override val background = viewProperty<ICoreView?>(null)
    override val foreground = viewProperty<ICoreView?>(null)

    override val paddingTop = viewProperty(CoreDimensionZero)
    override val paddingBottom = viewProperty(CoreDimensionZero)
    override val paddingStart = viewProperty(CoreDimensionZero)
    override val paddingEnd = viewProperty(CoreDimensionZero)
    override val indentTop = viewProperty(CoreDimensionZero)
    override val indentBottom = viewProperty(CoreDimensionZero)
    override val indentStart = viewProperty(CoreDimensionZero)
    override val indentEnd = viewProperty(CoreDimensionZero)
    override val shadow = viewProperty(CoreDimensionZero)
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

