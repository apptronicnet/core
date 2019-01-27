package net.apptronic.common.core.mvvm.viewmodel

fun <T : ViewModel> T?.setupView(block: T.() -> Unit) {
    this?.apply(block)
}