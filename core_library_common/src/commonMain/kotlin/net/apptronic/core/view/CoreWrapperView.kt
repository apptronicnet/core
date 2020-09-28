package net.apptronic.core.view

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.watch
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.value
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.context.CoreViewContext

/**
 * Creates container which wraps some dynamic view content
 */
class CoreWrapperView internal constructor(context: CoreViewContext) : CoreView(context) {

    private val wrapperBuilder = CoreWrapperBuilder(context)

    val wrappedView = value<ICoreView?>(null)

    init {
        wrappedView.watch().forEachReplacedValue {
            it?.context?.terminate()
        }
    }

    val transitionSpec = value<Any?>(null)

    private inner class CoreWrapperBuilder(override val context: CoreViewContext) : CoreViewBuilder {

        override fun <T : ICoreView> nextView(constructor: (CoreViewContext) -> T, builder: T.() -> Unit): T {
            return super.nextView(constructor, builder).also {
                wrappedView.set(it)
            }
        }

    }

    fun <T> content(source: Entity<T>, builder: CoreViewBuilder.(T) -> Unit) {
        source.subscribe {
            wrapperBuilder.builder(it)
        }
    }

    fun transitionSpec(value: Any?) {
        transitionSpec.set(value)
    }

    fun transitionSpec(source: Entity<Any?>) {
        transitionSpec.setAs(source)
    }

}

fun CoreViewBuilder.wrapperView(builder: CoreWrapperView.() -> Unit) {
    nextView(::CoreWrapperView, builder)
}