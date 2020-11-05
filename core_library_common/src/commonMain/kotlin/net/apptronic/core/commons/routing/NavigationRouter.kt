package net.apptronic.core.commons.routing

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual

interface NavigationRouter<T> {

    @Deprecated("No clear interface", replaceWith = ReplaceWith("sendCommandsSync(commands)"))
    fun sendCommands(vararg commands: T) = sendCommandsSync(*commands)

    @Deprecated("No clear interface", replaceWith = ReplaceWith("sendCommandsBroadcastSync(commands)"))
    fun sendCommandsBroadcast(vararg commands: T) = sendCommandsBroadcastSync(*commands)

    /**
     * Send a set of commands to be handled by registered [NavigationHandler]s.
     *
     * Commands will be handled by first [NavigationHandler] which supports it's execution according to priority.
     */
    fun sendCommandsSync(vararg commands: T)

    /**
     * Send a set of commands to be handled by registered [NavigationHandler]s.
     *
     * Commands will be handled by all [NavigationHandler]s which supports it's execution.
     */
    fun sendCommandsBroadcastSync(vararg commands: T)

    /**
     * Same as [sendCommandsSync] but doing it asynchronously.
     */
    fun sendCommandsAsync(vararg commands: T)

    /**
     * Same as [sendCommandsBroadcastSync] but doing it asynchronously.
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