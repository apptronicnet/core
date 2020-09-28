package net.apptronic.core.view.widgets

import net.apptronic.core.component.value
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

    override val text = value("")

    override val textColor = value(Black)

    override val textSize = value<Number>(16)

    override val onClick = value({})

    override val onLongClick = value({})

    override val contentAlignmentHorizontal = value<HorizontalAlignment>(DefaultAlignment)

    override val contentAlignmentVertical = value<VerticalAlignment>(DefaultAlignment)

    override val isEnabled = value(true)

}

fun CoreViewBuilder.buttonTextView(builder: CoreTextButtonView.() -> Unit): CoreTextButtonView {
    return nextView(::CoreTextButtonView, builder)
}