package net.apptronic.core.component.entity.behavior

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.coroutines.CoroutineLauncher
import net.apptronic.core.component.coroutines.coroutineLaunchers
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.not
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.subscribeSuspend
import net.apptronic.core.component.entity.switchContext

class OrElseSubscription {

    internal var action: (() -> Unit)? = null

    infix fun orElseDo(action: () -> Unit) {
        this.action = action
    }

}

class OrElseSuspendSubscription(private val coroutineLauncher: CoroutineLauncher) {

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
    val orElse = OrElseSuspendSubscription(coroutineLaunchers().scoped)
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