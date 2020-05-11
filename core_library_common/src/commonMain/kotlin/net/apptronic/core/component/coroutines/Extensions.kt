package net.apptronic.core.component.coroutines

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context

interface CoroutineLaunchers {

    /**
     * See [coroutineLauncherGlobal]
     */
    val global: CoroutineLauncher

    /**
     * See [coroutineLauncherLocal]
     */
    val local: CoroutineLauncher

    /**
     * See [coroutineLauncherScoped]
     */
    val scoped: CoroutineLauncher

}

fun Component.coroutineLaunchers(): CoroutineLaunchers {
    return CoroutineLaunchersImpl(context)
}

private class CoroutineLaunchersImpl(private val context: Context) : CoroutineLaunchers {

    override val global: CoroutineLauncher
        get() = context.coroutineLauncherGlobal()

    override val local: CoroutineLauncher
        get() = context.coroutineLauncherLocal()

    override val scoped: CoroutineLauncher
        get() = context.coroutineLauncherScoped()

}