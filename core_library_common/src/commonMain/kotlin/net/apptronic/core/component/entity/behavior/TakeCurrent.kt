package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.subscribe

fun <T> Entity<T>.takeCurrent(action: (T) -> Unit) {
    takeFirst().subscribe(action).unsubscribe()
}