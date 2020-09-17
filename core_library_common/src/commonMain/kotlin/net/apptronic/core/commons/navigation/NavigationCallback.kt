package net.apptronic.core.commons.navigation

import net.apptronic.core.UnderDevelopment

@UnderDevelopment
interface NavigationCallback<T> {

    companion object {
        val NAVIGATION_PRIORITY_LOWEST = 10
        val NAVIGATION_PRIORITY_LOW = 100
        val NAVIGATION_PRIORITY_DEFAULT = 1000
        val NAVIGATION_PRIORITY_HIGH = 10000
        val NAVIGATION_PRIORITY_HIGHEST = 1000000
    }

    val navigationCallbackPriority: Int
        get() = NAVIGATION_PRIORITY_DEFAULT

    fun onNavigationEvent(event: T): Boolean

}