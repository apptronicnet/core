package net.apptronic.core.view

import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference
import net.apptronic.core.view.context.CoreViewContext

/**
 * Creates container which wraps some dynamic view content
 */
class CoreWrapperView internal constructor(context: CoreViewContext) : CoreView(context) {

    private val wrapperBuilder = CoreWrapperBuilder(context)

    val wrappedView = viewProperty<ICoreView?>(null) {
        it?.context?.terminate()
    }

    val transitionSpec = viewProperty<Any?>(null)

    private inner class CoreWrapperBuilder(override val context: CoreViewContext) : CoreViewBuilder {

        override fun <T : ICoreView> nextView(constructor: (CoreViewContext) -> T, builder: T.() -> Unit): T {
            return super.nextView(constructor, builder).also {
                wrappedView.set(it)
            }
        }

    }

    fun <T> content(source: Entity<T>, builder: CoreViewBuilder.(T) -> Unit) {
        source.subscribe(context) {
            wrapperBuilder.builder(it)
        }
    }

    fun transitionSpec(value: Any?) {
        transitionSpec.set(value)
    }

    fun transitionSpec(source: Entity<Any?>) {
        source.subscribe(context) {
            transitionSpec.set(it)
        }
    }

    fun transitionSpec(value: DynamicReference<Any?>) {
        value.subscribeWith(context) {
            transitionSpec.set(it)
        }
    }

    fun transitionSpec(source: DynamicEntityReference<Any?, Entity<Any?>>) {
        source.subscribeWith(context) {
            transitionSpec(it)
        }
    }

}

fun CoreViewBuilder.wrapperView(builder: CoreWrapperView.() -> Unit) {
    nextView(::CoreWrapperView, builder)
}