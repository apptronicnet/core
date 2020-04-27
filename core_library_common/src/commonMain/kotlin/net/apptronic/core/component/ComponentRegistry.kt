package net.apptronic.core.component

import net.apptronic.core.base.SerialIdGenerator

object ComponentRegistry {

    private val idGenerator by lazy {
        SerialIdGenerator()
    }

    fun nextId(): Long {
        return idGenerator.nextId()
    }

}