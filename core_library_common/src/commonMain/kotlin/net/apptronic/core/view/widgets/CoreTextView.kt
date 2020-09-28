package net.apptronic.core.view.widgets

import net.apptronic.core.component.value
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.properties.Black
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment
import net.apptronic.core.view.widgets.commons.ICoreTextView

open class CoreTextView internal constructor(context: CoreViewContext) : CoreView(context), ICoreTextView {

    override var text = value("")

    override var textColor = value(Black)

    override var textSize = value<Number>(16)

    override var contentAlignmentHorizontal = value<HorizontalAlignment>(DefaultAlignment)

    override var contentAlignmentVertical = value<VerticalAlignment>(DefaultAlignment)

}

fun CoreViewBuilder.textView(builder: CoreTextView.() -> Unit): CoreTextView {
    return nextView(::CoreTextView, builder)
}