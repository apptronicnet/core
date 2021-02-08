package net.apptronic.core.base

import kotlin.native.concurrent.AtomicLong

actual class SerialIdGenerator {

    private var value = AtomicLong(0)

    actual fun nextId(): Long {
        return value.addAndGet(1)
    }

}