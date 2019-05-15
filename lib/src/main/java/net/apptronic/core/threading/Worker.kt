package net.apptronic.core.threading

interface Worker {

    fun execute(action: () -> Unit)

}
