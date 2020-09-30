package net.apptronic.core.view

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.watch
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.value

class ViewProperty<T> internal constructor(
        private val context: Context, initialValue: T, private val onRecycle: ((T) -> Unit)?
) {

    private val entity = context.value(initialValue)

    init {
        if (onRecycle != null) {
            entity.watch().forEachRecycledValue(onRecycle)
        }
    }

    internal fun setValue(value: T) {
        entity.set(value)
    }

    internal fun setValue(source: Entity<T>) {
        entity.setAs(source)
    }

    fun get(): T {
        return entity.get()
    }

    internal fun subscribeWith(targetContext: Context, callback: (T) -> Unit) {
        entity.subscribe(targetContext, callback)
    }

}
