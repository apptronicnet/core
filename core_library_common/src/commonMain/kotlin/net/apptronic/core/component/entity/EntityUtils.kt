package net.apptronic.core.component.entity

import net.apptronic.core.component.context.Context

/**
 * Get context instance for multiple sources. Verifies that all entities are in same context.
 */
fun collectContext(vararg entities: Entity<*>): Context {
    val context = entities[0].context.getToken()
    entities.forEach {
        if (context !== it.context.getToken()) {
            throw IllegalArgumentException("Function cannot use arguments from different contexts")
        }
    }
    return context
}