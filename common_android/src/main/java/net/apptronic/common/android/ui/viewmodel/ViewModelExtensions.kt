package net.apptronic.common.android.ui.viewmodel

fun <T : ViewModel> T?.setupView(block: T.() -> Unit) {
    this?.apply(block)
}