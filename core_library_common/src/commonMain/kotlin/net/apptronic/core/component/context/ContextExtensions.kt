package net.apptronic.core.component.context

fun Context.getGlobalContext(): Context {
    var global = this
    var complete = false
    do {
        global.parent?.let {
            global = it
        } ?: run {
            complete = true
        }
    } while (!complete)
    return global
}