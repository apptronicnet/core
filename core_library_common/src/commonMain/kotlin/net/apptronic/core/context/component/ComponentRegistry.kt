package net.apptronic.core.context.component

import net.apptronic.core.base.SerialIdGenerator

object ComponentRegistry {

    private val idGenerator = SerialIdGenerator()

    fun nextId(): Long {
        return idGenerator.nextId()
    }

}