package net.apptronic.core.view.widgets

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ICoreViewBuilder
import net.apptronic.core.view.commons.ICoreTextView
import net.apptronic.core.view.properties.ColorBlack
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

open class CoreTextView internal constructor() : CoreView(), ICoreTextView {

    override var text = viewProperty("")

    override var textColor = viewProperty(ColorBlack)

    override var fontWeight = viewProperty(Regular)

    override var textSize = viewProperty<Number>(16)

    override var contentAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)

    override var contentAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

}

fun ICoreViewBuilder.textView(builder: CoreTextView.() -> Unit): CoreTextView {
    return onNextView(CoreTextView(), builder)
}