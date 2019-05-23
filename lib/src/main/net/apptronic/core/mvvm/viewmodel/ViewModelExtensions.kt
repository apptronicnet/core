package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.Component

fun <T : Component> T?.setupView(block: T.() -> Unit) {
    this?.apply(block)
}