package net.apptronic.core.commons.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual

@UnderDevelopment
interface NavigationRouter<T> {

    /**
     * Send a set of commands to be handled by registered [NavigationHandler]s.
     *
     * Commands will be handled by first [NavigationHandler] which supports it's execution according to priority.
     */
    fun sendCommands(vararg commands: T)

    /**
     * Send a set of commands to be handled by registered [NavigationHandler]s.
     *
     * Commands will be handled by all [NavigationHandler]s which supports it's execution.
     */
    fun sendCommandsBroadcast(vararg commands: T)

    /**
     * Same as [sendCommands] but doing it asynchronously.
     */
    fun sendCommandsAsync(vararg commands: T)

    /**
     * Same as [sendCommandsBroadcast] but doing it asynchronously.
     */
    fun sendCommandsBroadcastAsync(vararg commands: T)

    /**
     * Registers [handler] for navigation commands in specified [context].
     */
    fun registerHandler(context: Context, handler: NavigationHandler<T>)

    fun <E> registerHandler(handlerAndContextual: E) where E : NavigationHandler<T>, E : Contextual {
        registerHandler(handlerAndContextual.context, handlerAndContextual)
    }

}