package net.apptronic.common.core.mvvm.viewmodel

import net.apptronic.common.core.component.Component

fun <T : Component> T?.setupView(block: T.() -> Unit) {
    this?.apply(block)
}