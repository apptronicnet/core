package net.apptronic.core.base

actual class Synchronized actual constructor() {

    actual fun <R> run(block: () -> R): R {
        return synchronized(this, block)
    }

}