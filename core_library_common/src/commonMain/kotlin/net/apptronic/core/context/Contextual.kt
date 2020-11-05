package net.apptronic.core.context

import net.apptronic.core.context.di.DependencyProvider

interface Contextual {

    val context: Context

    val dependencyProvider: DependencyProvider
        get() = context.dependencyDispatcher

}