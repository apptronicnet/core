package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.ISynchronized

actual class Synchronized : ISynchronized {

    private val lock = Object()

    override fun <R> executeBlock(block: () -> R): R {
        return synchronized(lock) {
            block.invoke()
        }
    }

}