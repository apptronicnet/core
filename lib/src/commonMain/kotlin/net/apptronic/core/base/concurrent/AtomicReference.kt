package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.IAtomicReference

/**
 * Reference which guarantees access to same value for different threads
 */
expect class AtomicReference<T>(initialValue: T) : IAtomicReference<T> {

}