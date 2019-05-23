package net.apptronic.core.platform

actual class Synchronized actual constructor() {

    actual fun <R> run(block: () -> R): R {
        return synchronized(this, block)
    }

}