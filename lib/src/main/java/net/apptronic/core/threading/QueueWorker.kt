package net.apptronic.core.threading

import net.apptronic.core.base.AtomicEntity
import net.apptronic.core.base.collections.LinkedQueue
import net.apptronic.core.component.platform.PlatformHandler

internal class QueueWorker(
    private val platformHandler: PlatformHandler,
    private val threadsCount: Int
) : Worker {

    private val threadsActive = AtomicEntity(0)
    private val actions = AtomicEntity(LinkedQueue<Action>())

    override fun execute(action: Action) {
        actions.perform {
            get().add(action)
        }
        threadsActive.perform {
            if (get() < threadsCount) {
                set(get() + 1)
                platformHandler.runInNewThread {
                    executeQueue()
                }
            }
        }
    }

    private fun executeQueue() {
        var action: Action?
        do {
            action = actions.perform {
                get().take()
            }
            action?.execute()
        } while (action != null)
        threadsActive.perform {
            set(get() - 1)
        }
    }

}