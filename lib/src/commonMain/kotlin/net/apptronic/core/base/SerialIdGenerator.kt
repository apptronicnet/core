package net.apptronic.core.base

import net.apptronic.core.base.concurrent.AtomicEntity

class SerialIdGenerator(initialValue: Long = 0) {

    private val entity = AtomicEntity(initialValue)

    fun nextId(): Long {
        return entity.perform {
            val current = get()
            set(current + 1)
            return@perform current
        }
    }

}