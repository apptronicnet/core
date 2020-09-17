package net.apptronic.core.commons.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context

@UnderDevelopment
interface NavigationRouter<T> {

    fun sendEvent(vararg events: T)

    fun sendBroadcastEvent(vararg events: T)

    fun sendEventsAsync(vararg events: T)

    fun sendBroadcastEventAsync(vararg events: T)

    fun registerCallback(context: Context, callback: NavigationCallback<T>)

}