package net.apptronic.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlin.coroutines.CoroutineContext

class ManualDispatcher : CoroutineDispatcher() {

    fun runAwaiting() {
        awaitingBlocks.forEach {
            it.run()
        }
        awaitingBlocks.clear()
    }

    private val awaitingBlocks = mutableListOf<Runnable>()

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        awaitingBlocks.add(block)
    }

}