package net.apptronic.core.commons.routing

import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope

class BasicNavigationRouter<T>(context: Context) : Component(context), NavigationRouter<T> {

    private inner class PriorityHandlers(val priority: Int) : Comparable<PriorityHandlers> {
        val list = mutableListOf<NavigationHandler<T>>()

        override fun compareTo(other: PriorityHandlers): Int {
            return other.priority.compareTo(priority)
        }

        fun addHandler(context: Context, handler: NavigationHandler<T>) {
            list.add(0, handler)
            context.lifecycle.getActiveStage()?.doOnExit {
                removeHandler(handler)
            } ?: removeHandler(handler)
        }

        fun removeHandler(handler: NavigationHandler<T>) {
            list.remove(handler)
            if (list.isEmpty()) {
                priorityHandlersList.remove(this)
            }
        }

    }

    private val priorityHandlersList = mutableListOf<PriorityHandlers>()

    private fun sendCommandsInternal(commands: List<T>, broadcast: Boolean) {
        val allHandlers = priorityHandlersList.flatMap { it.list }
        for (command in commands) {
            sendCommand(command, broadcast, allHandlers)
        }
    }

    private fun sendCommand(command: T, broadcast: Boolean, allHandlers: List<NavigationHandler<T>>) {
        for (handler in allHandlers) {
            if (handler.onNavigationCommand(command) && !broadcast) {
                return
            }
        }
    }

    override fun sendCommandsSync(vararg commands: T) {
        sendCommandsInternal(commands.toList(), false)
    }

    override fun sendCommandsAsync(vararg commands: T) {
        contextCoroutineScope.launch {
            sendCommandsSync(*commands)
        }
    }

    override fun sendCommandsBroadcastSync(vararg commands: T) {
        sendCommandsInternal(commands.toList(), true)
    }

    override fun sendCommandsBroadcastAsync(vararg commands: T) {
        contextCoroutineScope.launch {
            sendCommandsInternal(commands.toList(), true)
        }
    }

    override fun registerHandler(context: Context, handler: NavigationHandler<T>) {
        (priorityHandlersList.firstOrNull {
            it.priority == handler.navigationHandlerPriority
        } ?: PriorityHandlers(handler.navigationHandlerPriority).also {
            priorityHandlersList.add(it)
            priorityHandlersList.sort()
        }).addHandler(context, handler)
    }

}