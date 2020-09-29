package net.apptronic.core.view.widgets

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.dimension
import net.apptronic.core.view.properties.Black
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment
import net.apptronic.core.view.widgets.commons.ICoreTextView

open class CoreTextView internal constructor(context: CoreViewContext) : CoreView(context), ICoreTextView {

    override var text = viewProperty("")

    override var textColor = viewProperty(Black)

    override var textSize = viewProperty<CoreDimension>(16.dimension)

    override var contentAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)

    override var contentAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

}

fun CoreViewBuilder.textView(builder: CoreTextView.() -> Unit): CoreTextView {
    return nextView(::CoreTextView, builder)
}