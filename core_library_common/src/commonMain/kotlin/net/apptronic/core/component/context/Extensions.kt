package net.apptronic.core.component.context

fun Context.close() {
    lifecycle.terminate()
}