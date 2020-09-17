package net.apptronic.core.commons.navigation

import kotlinx.coroutines.launch
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.contextCoroutineScope
import net.apptronic.core.component.extensions.BaseComponent

@UnderDevelopment
class BaseNavigationRouter<T>(context: Context) : BaseComponent(context), NavigationRouter<T> {

    private inner class PriorityCallbacks(val priority: Int) : Comparable<PriorityCallbacks> {
        val callbacks = mutableListOf<NavigationCallback<T>>()
        override fun compareTo(other: PriorityCallbacks): Int {
            return other.priority.compareTo(priority)
        }

        fun addCallback(context: Context, callback: NavigationCallback<T>) {
            callbacks.add(0, callback)
            context.lifecycle.getActiveStage()?.doOnExit {
                removeCallback(callback)
            } ?: removeCallback(callback)
        }

        fun removeCallback(callback: NavigationCallback<T>) {
            callbacks.remove(callback)
            if (callbacks.isEmpty()) {
                priorityCallbacks.remove(this)
            }
        }
    }

    private val priorityCallbacks = mutableListOf<PriorityCallbacks>()

    private fun sendEventInternal(event: T, broadcast: Boolean) {
        for (priorityCallback in priorityCallbacks) {
            for (callback in priorityCallback.callbacks) {
                if (callback.onNavigationEvent(event) && !broadcast) {
                    return
                }
            }
        }
    }

    override fun sendEvent(vararg events: T) {
        events.forEach {
            sendEventInternal(it, it is BroadcastNavigationEvent)
        }
    }

    override fun sendEventsAsync(vararg events: T) {
        contextCoroutineScope.launch {
            sendEvent(*events)
        }
    }

    override fun sendBroadcastEvent(vararg events: T) {
        events.forEach {
            sendEventInternal(it, true)
        }
    }

    override fun sendBroadcastEventAsync(vararg events: T) {
        contextCoroutineScope.launch {
            sendBroadcastEvent(*events)
        }
    }

    override fun registerCallback(context: Context, callback: NavigationCallback<T>) {
        (priorityCallbacks.firstOrNull {
            it.priority == callback.navigationCallbackPriority
        } ?: PriorityCallbacks(callback.navigationCallbackPriority).also {
            priorityCallbacks.add(it)
            priorityCallbacks.sort()
        }).addCallback(context, callback)
    }

}