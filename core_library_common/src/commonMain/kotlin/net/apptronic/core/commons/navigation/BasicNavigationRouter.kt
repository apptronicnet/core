package net.apptronic.core.commons.navigation

import kotlinx.coroutines.launch
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.contextCoroutineScope
import net.apptronic.core.component.extensions.BaseComponent

class BasicNavigationRouter<T>(context: Context) : BaseComponent(context), NavigationRouter<T> {

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

    override fun sendCommands(vararg commands: T) {
        sendCommandsInternal(commands.toList(), false)
    }

    override fun sendCommandsAsync(vararg commands: T) {
        contextCoroutineScope.launch {
            sendCommands(*commands)
        }
    }

    override fun sendCommandsBroadcast(vararg commands: T) {
        sendCommandsInternal(commands.toList(), true)
    }

    override fun sendCommandsBroadcastAsync(vararg commands: T) {
        contextCoroutineScope.launch {
            sendCommandsBroadcast(*commands)
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