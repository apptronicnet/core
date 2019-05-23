package net.apptronic.core.component.context

import net.apptronic.core.base.Logger
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.platform.PlatformHandler
import net.apptronic.core.threading.Scheduler

/**
 * Base instance for working with framework [Context] represents logical process in application.
 * All application using framework should be built as tree of [Context] instances. Each context
 * contains [Lifecycle], specifies own [Scheduler] and provides [DependencyProvider]
 */
interface Context {

    /**
     * User for cases when thing is used as only context wrapper. Returns core instance
     * of context for direct usage and comparison when needed.
     */
    fun getToken(): Context {
        return this
    }

    /**
     * Logger is for logging internals of context
     */
    fun getLogger(): Logger

    /**
     * Get [Lifecycle] of current [Context]
     */
    fun getLifecycle(): Lifecycle

    /**
     * Get [Scheduler] for current [Context]
     */
    fun getScheduler(): Scheduler

    /**
     * Get [DependencyProvider] for current [Context]
     */
    fun getProvider(): DependencyProvider

    /**
     * Get [PlatformHandler] instance
     */
    fun getPlatformHandler(): PlatformHandler

}
