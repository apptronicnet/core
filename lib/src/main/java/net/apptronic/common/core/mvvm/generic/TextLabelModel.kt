package net.apptronic.common.core.mvvm.generic

import net.apptronic.common.core.component.Component

class TextLabelModel(parent: Component) : Component.SubModel(parent) {

    val text = value<String>()
    val textColor = value<Color>()

}