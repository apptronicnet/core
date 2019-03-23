package net.apptronic.common.core.base.threading

interface Worker {

    fun run(action: () -> Unit)

}
