package net.apptronic.core.android.platform

import net.apptronic.core.platform.Synchronized

class JavaSynchronized : Synchronized {

    override fun <R> run(block: () -> R): R {
        return synchronized(this, block)
    }

}