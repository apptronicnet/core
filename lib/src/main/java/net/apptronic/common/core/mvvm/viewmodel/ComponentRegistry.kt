package net.apptronic.common.core.mvvm.viewmodel

import net.apptronic.common.core.base.SerialIdGenerator

object ComponentRegistry {

    private val idGenerator = SerialIdGenerator()

    fun nextId(): Long {
        return idGenerator.nextId()
    }

}