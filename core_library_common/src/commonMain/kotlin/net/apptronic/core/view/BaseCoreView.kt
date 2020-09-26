package net.apptronic.core.view

import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreDimensionZero
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
    }

    override val layoutAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)
    override val layoutAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

    override val width = viewProperty<CoreLayoutDimension>(FitToContentDimension)
    override val height = viewProperty<CoreLayoutDimension>(FitToContentDimension)

    override val background = viewProperty<CoreViewLayer?>(null)
    override val foreground = viewProperty<CoreViewLayer?>(null)

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

}

