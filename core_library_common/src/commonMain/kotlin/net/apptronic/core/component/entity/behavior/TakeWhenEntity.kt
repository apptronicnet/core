package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe

fun <T> Entity<T>.takeWhen(chooser: Entity<Boolean>, whileNotSet: Boolean = false): Entity<T> {
    return TakeWhenEntity(this, chooser, whileNotSet)
}

private class TakeWhenEntity<T>(
        private val source: Entity<T>,
        private val chooser: Entity<Boolean>,
        whileNotSet: Boolean = false
) : Entity<T> {

    private var chooserValue = whileNotSet

    init {
        chooser.subscribe {
            chooserValue = it
        }
    }

    override val context: Context = source.context

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return source.subscribe(context, ChooserObserver(observer))
    }

    private inner class ChooserObserver<T>(
            private val target: Observer<T>
    ) : Observer<T> {
        override fun notify(value: T) {
            if (chooserValue) {
                target.notify(value)
            }
        }
    }

}