package net.apptronic.core.entity.behavior

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.function.not

class OrElseSubscription {

    internal var action: (() -> Unit)? = null

    infix fun orElseDo(action: () -> Unit) {
        this.action = action
    }

}

class OrElseSuspendSubscription(private val coroutineScope: CoroutineScope) {

    internal var action: (suspend CoroutineScope.() -> Unit)? = null

    infix fun orElseDo(action: suspend CoroutineScope.() -> Unit) {
        this.action = action
    }

}

fun Contextual.doWhen(entity: Entity<Boolean>, action: () -> Unit): OrElseSubscription {
    val orElse = OrElseSubscription()
    entity.subscribe(context) {
        if (it) {
            action()
        } else {
            orElse.action?.invoke()
        }
    }
    return orElse
}

fun Contextual.doWhenSuspend(entity: Entity<Boolean>, action: () -> Unit): OrElseSuspendSubscription {
    val orElse = OrElseSuspendSubscription(lifecycleCoroutineScope)
    entity.subscribeSuspend(context) {
        if (it) {
            action()
        } else {
            orElse.action?.invoke(this)
        }
    }
    return orElse
}

fun Contextual.doWhenNot(entity: Entity<Boolean>, action: () -> Unit): OrElseSubscription {
    return doWhen(entity.switchContext(context).not(), action)
}

fun Contextual.doWhenNotSuspend(entity: Entity<Boolean>, action: () -> Unit): OrElseSuspendSubscription {
    return doWhenSuspend(entity.switchContext(context).not(), action)
}