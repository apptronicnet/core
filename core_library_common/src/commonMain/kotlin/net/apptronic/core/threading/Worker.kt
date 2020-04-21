package net.apptronic.core.threading

@Deprecated("Should use coroutines")
interface Worker {

    fun execute(action: Action)

}