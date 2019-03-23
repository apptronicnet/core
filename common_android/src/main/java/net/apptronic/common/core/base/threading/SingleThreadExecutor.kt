package net.apptronic.common.core.base.threading

import net.apptronic.common.core.base.AtomicEntity
import kotlin.concurrent.thread

/**
 * [Worker] implementation which executes actions one after another in single thread.
 */
internal class SerialWorker(
    private val maxCount: Int = NO_LIMIT
) : Worker {

    companion object {
        const val NO_LIMIT = -1
    }

    private val isRunning = AtomicEntity(false)

    private val actions = mutableListOf<() -> Unit>()

    override fun run(action: () -> Unit) {
        isRunning.perform {
            actions.add(action)
            if (maxCount > 0) {
                while (actions.size > maxCount) {
                    actions.removeAt(0)
                }
            }
            if (!get()) {
                set(true)
                thread(start = true) {
                    executeQueue()
                }
            }
        }
    }

    private fun executeQueue() {
        var next: (() -> Unit)? = null
        do {
            isRunning.perform {
                next = if (actions.isNotEmpty()) actions.removeAt(0) else null
                if (next == null) {
                    set(false)
                }
            }
            next?.invoke()
        } while (next != null)
    }

}