package net.apptronic.core.base

import java.util.concurrent.atomic.AtomicLong

actual class SerialIdGenerator {

    private var value = AtomicLong(0)

    actual fun nextId(): Long {
        return value.getAndIncrement()
    }

}