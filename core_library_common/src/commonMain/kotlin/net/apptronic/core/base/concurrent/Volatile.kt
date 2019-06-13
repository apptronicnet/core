package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.IVolatile

/**
 * Reference which guarantees access to same value for different threads
 */
expect class Volatile<T>(initialValue: T) : IVolatile<T> {

}