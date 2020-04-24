package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe

fun <T> Entity<T>.suspendOn(timeInMillis: Long): Entity<T> {
    return SuspendEntity(this) { timeInMillis }
}

fun <T> Entity<T>.suspendOn(timeInMillisProvider: (T) -> Long): Entity<T> {
    return SuspendEntity(this, timeInMillisProvider)
}

private class SuspendEntity<T>(
        private val source: Entity<T>,
        private val timeInMillisProvider: (T) -> Long
) : Entity<T> {

    override val context: Context = source.context

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        val coroutineLauncher = context.coroutineLauncherScoped()
        return source.subscribe(context) {
            coroutineLauncher.launch {
                val timeInMillis = timeInMillisProvider.invoke(it)
                if (timeInMillis > 0L) {
                    kotlinx.coroutines.delay(timeInMillis)
                }
                observer.notify(it)
            }
        }
    }

}