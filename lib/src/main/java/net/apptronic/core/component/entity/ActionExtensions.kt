package net.apptronic.core.component.entity

import net.apptronic.core.component.entity.functions.variants.not

class OrElseSubscription() {

    internal var action: (() -> Unit)? = null

    infix fun orElseDo(action: () -> Unit) {
        this.action = action
    }

}

fun doWhen(predicate: Predicate<Boolean>, action: () -> Unit): OrElseSubscription {
    val orElse = OrElseSubscription()
    predicate.subscribe {
        if (it) {
            action()
        } else {
            orElse.action?.invoke()
        }
    }
    return orElse
}

fun doWhenNot(predicate: Predicate<Boolean>, action: () -> Unit): OrElseSubscription {
    return doWhen(predicate.not(), action)
}

fun <T, D : Predicate<T>> D.setup(block: D.() -> Any): D {
    this.block()
    return this
}