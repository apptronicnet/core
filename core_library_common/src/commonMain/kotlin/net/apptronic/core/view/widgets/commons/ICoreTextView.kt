package net.apptronic.core.view.widgets.commons

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.CoreContentView
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.binder.SupportsTargetBridge
import net.apptronic.core.view.properties.CoreColor

interface ICoreTextView : CoreView, CoreContentView {

    val text: ViewProperty<String>

    val textColor: ViewProperty<CoreColor>

    val textSize: ViewProperty<Number>

    fun text(text: String) {
        this.text.set(text)
    }

    fun <T> SupportsTargetBridge<T>.text(bridger: T.() -> String) {
        text(readValue(bridger))
    }

    fun text(source: Observable<String>) {
        this.text.set(source)
    }

    fun <T> SupportsTargetBridge<T>.textFrom(bridger: T.() -> Observable<String>) {
        text(readObservable(bridger))
    }

    fun textColor(color: CoreColor) {
        this.textColor.set(color)
    }

    fun textColor(source: Observable<CoreColor>) {
        this.textColor.set(source)
    }

    fun <T> SupportsTargetBridge<T>.textColor(bridger: T.() -> Observable<CoreColor>) {
        textColor(readObservable(bridger))
    }

    fun textSize(color: Number) {
        this.textSize.set(color)
    }

    fun textSize(source: Observable<Number>) {
        this.textSize.set(source)
    }

    fun <T> SupportsTargetBridge<T>.textSize(bridger: T.() -> Observable<Number>) {
        textSize(readObservable(bridger))
    }

}