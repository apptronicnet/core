package net.apptronic.core.view.widgets

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.properties.Black
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment
import net.apptronic.core.view.widgets.commons.ICoreButtonView
import net.apptronic.core.view.widgets.commons.ICoreTextView

class CoreTextButtonView(context: CoreViewContext) : CoreView(context), ICoreButtonView, ICoreTextView {

    override val text = viewProperty("")

    override val textColor = viewProperty(Black)

    override val textSize = viewProperty<Number>(16)

    override val onClick = viewProperty({})

    override val onLongClick = viewProperty({})

    override val contentAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)

    override val contentAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

    override val isEnabled = viewProperty(true)

}

fun CoreViewBuilder.buttonTextView(builder: CoreTextButtonView.() -> Unit): CoreTextButtonView {
    return nextView(::CoreTextButtonView, builder)
}