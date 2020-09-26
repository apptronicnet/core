package net.apptronic.core.view.widgets

import net.apptronic.core.view.BaseCoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.properties.Black
import net.apptronic.core.view.properties.DefaultAlignment
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment
import net.apptronic.core.view.widgets.commons.ICoreButtonView
import net.apptronic.core.view.widgets.commons.ICoreTextView

class CoreTextButtonView(viewConfiguration: ViewConfiguration) : BaseCoreView(viewConfiguration), ICoreButtonView, ICoreTextView {

    override var text = viewProperty("")

    override var textColor = viewProperty(Black)

    override var textSize = viewProperty<Number>(16)

    override var onClick = viewProperty<() -> Any>({})

    override var onLongClick = viewProperty<() -> Any>({})

    override var contentAlignmentHorizontal = viewProperty<HorizontalAlignment>(DefaultAlignment)

    override var contentAlignmentVertical = viewProperty<VerticalAlignment>(DefaultAlignment)

    override var isEnabled = viewProperty(true)

}

fun CoreViewBuilder.buttonTextView(builder: CoreTextButtonView.() -> Unit) {
    nextView(CoreTextButtonView(viewConfiguration), builder)
}