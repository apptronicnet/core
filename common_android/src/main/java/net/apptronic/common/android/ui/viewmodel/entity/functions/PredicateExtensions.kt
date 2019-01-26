package net.apptronic.common.android.ui.viewmodel.entity.functions

import net.apptronic.common.android.ui.viewmodel.entity.functions.variants.not

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
