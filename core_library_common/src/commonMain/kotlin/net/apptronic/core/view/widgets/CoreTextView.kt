package net.apptronic.core.view.widgets

import net.apptronic.core.view.BaseCoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.properties.*

open class CoreTextView internal constructor(viewConfiguration: ViewConfiguration) : BaseCoreView(viewConfiguration), CoreText {

    override var text = viewProperty("")

    override var textColor = viewProperty(Black)

    override var textSize = viewProperty<Number>(16)

    override var contentAlignmentHorizontal: HorizontalAlignment = ToStart

    override var contentAlignmentVertical: VerticalAlignment = ToTop

}

fun CoreViewBuilder.textView(builder: CoreTextView.() -> Unit) {
    nextView(CoreTextView(viewConfiguration).also(builder))
}