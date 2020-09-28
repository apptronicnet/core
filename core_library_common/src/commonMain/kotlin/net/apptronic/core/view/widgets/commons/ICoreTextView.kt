package net.apptronic.core.view.widgets.commons

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.CoreContentView
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference
import net.apptronic.core.view.properties.CoreColor

interface ICoreTextView : ICoreView, CoreContentView {

    val text: ViewProperty<String>

    val textColor: ViewProperty<CoreColor>

    val textSize: ViewProperty<Number>

    fun text(value: String) {
        text.set(value)
    }

    fun text(source: Entity<String>) {
        text.set(source)
    }

    fun text(reference: DynamicReference<String>) {
        text.set(reference)
    }

    fun text(referenceSource: DynamicEntityReference<String, Entity<String>>) {
        text.set(referenceSource)
    }

    fun textColor(value: CoreColor) {
        textColor.set(value)
    }

    fun textColor(source: Entity<CoreColor>) {
        textColor.set(source)
    }

    fun textColor(reference: DynamicReference<CoreColor>) {
        textColor.set(reference)
    }

    fun textColor(referenceSource: DynamicEntityReference<CoreColor, Entity<CoreColor>>) {
        textColor.set(referenceSource)
    }

    fun textSize(value: Number) {
        textSize.set(value)
    }

    fun textSize(source: Entity<Number>) {
        textSize.set(source)
    }

    fun textSize(reference: DynamicReference<Number>) {
        textSize.set(reference)
    }

    fun textSize(referenceSource: DynamicEntityReference<Number, Entity<Number>>) {
        textSize.set(referenceSource)
    }

}