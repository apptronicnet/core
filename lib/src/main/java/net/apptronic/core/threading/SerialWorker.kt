package net.apptronic.core.threading

import net.apptronic.core.base.AtomicEntity
import net.apptronic.core.base.runInNewThread
import net.apptronic.core.component.platform.PlatformHandler

/**
 * [Worker] implementation which executes actions one after another in single thread.
 */
internal class SerialWorker(
    private val platformHandler: PlatformHandler,
    private val maxCount: Int = NO_LIMIT
) : Worker {

    companion object {
        const val NO_LIMIT = -1
    }

    private val isRunning = AtomicEntity(false)

    private val actions = mutableListOf<Action>()

    override fun execute(action: Action) {
        isRunning.perform {
            actions.add(action)
            if (maxCount > 0) {
                while (actions.size > maxCount) {
                    actions.removeAt(0)
                }
            }
            if (!get()) {
                set(true)
                runInNewThread {
                    executeQueue()
                }
            }
        }
    }

    private fun executeQueue() {
        var next: Action? = null
        do {
            isRunning.perform {
                next = if (actions.isNotEmpty()) actions.removeAt(0) else null
                if (next == null) {
                    set(false)
                }
            }
            next?.execute()
        } while (next != null)
    }

}