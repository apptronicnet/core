package net.apptronic.core.base

class SerialIdGenerator(initialValue: Long = 0) {

    private var value = initialValue

    fun nextId(): Long {
        return value++
    }

}