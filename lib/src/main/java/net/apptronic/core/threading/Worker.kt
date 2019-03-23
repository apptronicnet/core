package net.apptronic.core.threading

interface Worker {

    fun run(action: () -> Unit)

}
