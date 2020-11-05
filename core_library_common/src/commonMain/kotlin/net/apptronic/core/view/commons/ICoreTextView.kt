package net.apptronic.core.view.commons

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.entity.Entity
import net.apptronic.core.view.ICoreContentView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.properties.CoreColor
import net.apptronic.core.view.properties.FontWeight

@UnderDevelopment
interface ICoreTextView : ICoreView, ICoreContentView {

    val text: ViewProperty<String>

    val textColor: ViewProperty<CoreColor>

    val textSize: ViewProperty<Number>

    val fontWeight: ViewProperty<FontWeight>

    fun text(value: String) {
        text.set(value)
    }

    fun text(source: Entity<String>) {
        text.set(source)
    }

    fun textColor(value: CoreColor) {
        textColor.set(value)
    }

    fun textColor(source: Entity<CoreColor>) {
        textColor.set(source)
    }

    fun textSize(value: Number) {
        textSize.set(value)
    }

    fun textSize(source: Entity<Number>) {
        textSize.set(source)
    }

    fun fontWeight(value: FontWeight) {
        fontWeight.set(value)
    }

    fun fontWeight(source: Entity<FontWeight>) {
        fontWeight.set(source)
    }

}