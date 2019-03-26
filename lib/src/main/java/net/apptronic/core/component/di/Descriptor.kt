package net.apptronic.core.component.di

import net.apptronic.core.base.AtomicEntity

class Descriptor<T> {

    private companion object {
        val id = AtomicEntity<Int>(0)
    }

    private val descriptorId: Int = id.let {
        val value = it.get()
        it.set(value + 1)
        value
    }

    override fun hashCode(): Int {
        return descriptorId
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

}