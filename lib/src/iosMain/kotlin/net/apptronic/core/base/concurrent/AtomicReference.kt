package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.IAtomicReference

actual class AtomicReference<T> actual constructor(initialValue: T) : IAtomicReference<T> {

    // TODO temporary solution
    private var reference: T = initialValue

    override fun set(value: T) {
        reference = value
    }

    override fun get(): T {
        return reference
    }

}