package net.apptronic.core.view.widgets.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.view.CoreContentView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.properties.CoreColor

interface ICoreTextView : ICoreView, CoreContentView {

    val text: Value<String>

    val textColor: Value<CoreColor>

    val textSize: Value<Number>

    fun text(text: String) {
        this.text.set(text)
    }

    fun text(source: Entity<String>) {
        this.text.setAs(source)
    }

    fun textColor(color: CoreColor) {
        this.textColor.set(color)
    }

    fun textColor(source: Entity<CoreColor>) {
        this.textColor.setAs(source)
    }

    fun textSize(color: Number) {
        this.textSize.set(color)
    }

    fun textSize(source: Entity<Number>) {
        this.textSize.setAs(source)
    }

}