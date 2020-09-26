package net.apptronic.core.view

import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.dimension.FitToContentDimension
import net.apptronic.core.view.layer.CoreViewLayer
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment
import net.apptronic.core.view.properties.Visibility

abstract class BaseCoreView(override val viewConfiguration: ViewConfiguration) : CoreView {

    private val viewProperties = mutableListOf<ViewProperty<*>>()

    final override fun <T> viewProperty(initialValue: T): ViewProperty<T> {
        return ViewProperty(initialValue).also {
            viewProperties.add(it)
        }
    }

    private var isRecycledState = false

    override val isRecycled: Boolean
        get() = isRecycledState

    override fun recycle() {
        viewProperties.forEach { it.recycle() }
        background?.recycle()
        foreground?.recycle()
    }

    override var layoutAlignmentHorizontal: HorizontalAlignment = DefaultAlignment
    override var layoutAlignmentVertical: VerticalAlignment = DefaultAlignment

    override var width: CoreLayoutDimension = FitToContentDimension()
    override var height: CoreLayoutDimension = FitToContentDimension()

    override var background: CoreViewLayer? = null
    override var foreground: CoreViewLayer? = null

    override val paddingTop = viewProperty<Number>(0)
    override val paddingBottom = viewProperty<Number>(0)
    override val paddingStart = viewProperty<Number>(0)
    override val paddingEnd = viewProperty<Number>(0)
    override val indentTop = viewProperty<Number>(0)
    override val indentBottom = viewProperty<Number>(0)
    override val indentStart = viewProperty<Number>(0)
    override val indentEnd = viewProperty<Number>(0)
    override val shadow = viewProperty<Number>(0)
    override val visibility = viewProperty(Visibility.Visible)

}

