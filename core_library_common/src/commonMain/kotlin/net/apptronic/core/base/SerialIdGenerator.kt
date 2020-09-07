package net.apptronic.core.base

expect class SerialIdGenerator() {

    fun nextId(): Long

}