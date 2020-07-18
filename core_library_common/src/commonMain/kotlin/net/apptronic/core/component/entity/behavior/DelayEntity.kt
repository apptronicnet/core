package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.CoroutineLauncher
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.RelayEntity

fun <T> Entity<T>.delay(timeInMillis: Long): Entity<T> {
    return DelayEntity(this) { timeInMillis }
}

fun <T> Entity<T>.delay(timeInMillisProvider: (T) -> Long): Entity<T> {
    return DelayEntity(this, timeInMillisProvider)
}

private class DelayEntity<T>(
         source: Entity<T>,
        private val timeInMillisProvider: (T) -> Long
) : RelayEntity<T>(source) {

    override fun onNewObserver(targetContext: Context, observer: Observer<T>): Observer<T> {
        val coroutineLauncher = targetContext.coroutineLauncherScoped()
        return DelayObserver(observer, coroutineLauncher, timeInMillisProvider)
    }

    private class DelayObserver<T>(
            private val target: Observer<T>,
            private val coroutineLauncher: CoroutineLauncher,
            private val timeInMillisProvider: (T) -> Long
    ) : Observer<T> {

        override fun notify(value: T) {
            coroutineLauncher.launch {
                val timeInMillis = timeInMillisProvider.invoke(value)
                if (timeInMillis > 0L) {
                    kotlinx.coroutines.delay(timeInMillis)
                }
                target.notify(value)
            }
        }

    }

}