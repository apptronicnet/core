package net.apptronic.core.view.widgets

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ICoreViewBuilder
import net.apptronic.core.view.commons.ICoreButtonView
import net.apptronic.core.view.commons.ICoreTextView
import net.apptronic.core.view.properties.ColorBlack
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

class CoreTextButtonView : CoreView(), ICoreButtonView, ICoreTextView {

    override val text = viewProperty("")

    override val textColor = viewProperty(ColorBlack)

    override val textSize = viewProperty<Number>(16)

    override val fontWeight = viewProperty(Medium)

    override val onClick = viewProperty({})

    override val onLongClick = viewProperty({})

    override val contentAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)

    override val contentAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

    override val isEnabled = viewProperty(true)

}

fun ICoreViewBuilder.buttonTextView(builder: CoreTextButtonView.() -> Unit): CoreTextButtonView {
    return onNextView(CoreTextButtonView(), builder)
}