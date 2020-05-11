package net.apptronic.core.component.context

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.component.di.DependencyDispatcher
import net.apptronic.core.component.lifecycle.Lifecycle

/**
 * Base instance for working with framework [Context] represents logical process in application.
 * All application using framework should be built as tree of [Context] instances. Each context
 * contains [Lifecycle], specifies own [Scheduler] and provides [DependencyDispatcher]
 */
interface Context {

    val defaultDispatcher: CoroutineDispatcher

    fun getParent(): Context?

    /**
     * [Lifecycle] of current [Context]
     */
    val lifecycle: Lifecycle

    /**
     * Get [DependencyDispatcher] for current [Context]
     */
    fun dependencyDispatcher(): DependencyDispatcher

}
