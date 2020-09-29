package net.apptronic.core.view

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.dimension.DiscreteCoreDimension
import net.apptronic.core.view.dimension.PixelCoreDimension
import net.apptronic.core.view.properties.*

abstract class CoreView : ICoreView {

    private val themesList = mutableListOf<CoreViewStyle>()

    override val themes: List<CoreViewStyle>
        get() = themesList

    final override val FitToParent = CoreLayoutDimension.FitToParent
    final override val FitToContent = CoreLayoutDimension.FitToContent

    final override val Visible: Visibility = Visibility.Visible
    final override val Invisible: Visibility = Visibility.Invisible
    final override val Gone: Visibility = Visibility.Gone

    final override val Number.pixels: CoreDimension
        get() = PixelCoreDimension(this.toFloat())
    final override val Number.dimension: CoreDimension
        get() = DiscreteCoreDimension(this.toFloat())

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

    final override val Thin: FontWeight = FontWeight(100)
    final override val Light: FontWeight = FontWeight(200)
    final override val Book: FontWeight = FontWeight(300)
    final override val Regular: FontWeight = FontWeight(400)
    final override val Medium: FontWeight = FontWeight(500)
    final override val SemiBold: FontWeight = FontWeight(600)
    final override val Bold: FontWeight = FontWeight(700)
    final override val Heavy: FontWeight = FontWeight(800)
    final override val UltraHeavy: FontWeight = FontWeight(900)

    fun <T> viewProperty(initialValue: T, onRecycle: ((T) -> Unit)? = null): ViewProperty<T> {
        return ViewProperty(context, initialValue, onRecycle)
    }

    protected fun <T> ViewProperty<T>.set(value: T) {
        this.setValue(value)
    }

    protected fun <T> ViewProperty<T>.set(entity: Entity<T>) {
        entity.subscribe(context) {
            this.setValue(it)
        }
    }

    final override val context: CoreViewContext = CoreViewContext()

    override val layoutAlignmentHorizontal = viewProperty<HorizontalAlignment?>(null)
    override val layoutAlignmentVertical = viewProperty<VerticalAlignment?>(null)

    override val width = viewProperty<CoreLayoutDimension>(FitToContent)
    override val height = viewProperty<CoreLayoutDimension>(FitToContent)

    override val paddingTop = viewProperty<Number>(0)
    override val paddingBottom = viewProperty<Number>(0)
    override val paddingLeft = viewProperty<Number>(0)
    override val paddingRight = viewProperty<Number>(0)
    override val paddingStart = viewProperty<Number>(0)
    override val paddingEnd = viewProperty<Number>(0)

    override val indentTop = viewProperty<Number>(0)
    override val indentBottom = viewProperty<Number>(0)
    override val indentLeft = viewProperty<Number>(0)
    override val indentRight = viewProperty<Number>(0)
    override val indentStart = viewProperty<Number>(0)
    override val indentEnd = viewProperty<Number>(0)

    override val shadow = viewProperty<Number>(0)
    override val visibility = viewProperty(Visible)

    final override fun style(vararg style: CoreViewStyle) {
        style.forEach {
            it.applyTo(this)
        }
    }

    final override fun theme(vararg theme: CoreViewStyle) {
        themesList.addAll(theme)
        style(*theme)
    }

}

