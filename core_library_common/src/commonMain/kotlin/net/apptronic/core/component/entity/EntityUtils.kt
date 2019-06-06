package net.apptronic.core.component.entity

import net.apptronic.core.component.context.Context

fun collectContext(vararg entities: Entity<*>): Context {
    val context = entities[0].getContext().getToken()
    entities.forEach {
        if (context !== it.getContext().getToken()) {
            throw IllegalArgumentException("Function cannot use arguments from different contexts")
        }
    }
    return context
}