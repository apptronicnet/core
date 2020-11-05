package net.apptronic.core.entity

import net.apptronic.core.context.Context

/**
 * Get context instance for multiple sources. Verifies that all entities are in same context.
 */
fun collectContext(vararg entities: Entity<*>): Context {
    val context = entities[0].context
    entities.forEach {
        if (context !== it.context) {
            throw IllegalArgumentException("Function cannot use arguments from different contexts")
        }
    }
    return context
}