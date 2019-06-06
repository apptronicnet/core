package net.apptronic.core.component.entity.extensions

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.not

class OrElseSubscription() {

    internal var action: (() -> Unit)? = null

    infix fun orElseDo(action: () -> Unit) {
        this.action = action
    }

}

fun doWhen(entity: Entity<Boolean>, action: () -> Unit): OrElseSubscription {
    val orElse = OrElseSubscription()
    entity.subscribe {
        if (it) {
            action()
        } else {
            orElse.action?.invoke()
        }
    }
    return orElse
}

fun doWhenNot(entity: Entity<Boolean>, action: () -> Unit): OrElseSubscription {
    return doWhen(entity.not(), action)
}

fun <T, D : Entity<T>> D.setup(block: D.() -> Any): D {
    this.block()
    return this
}