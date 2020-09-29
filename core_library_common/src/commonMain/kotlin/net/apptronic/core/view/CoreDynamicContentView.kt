package net.apptronic.core.view

import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.entity.Entity

/**
 * Creates container which wraps some dynamic view content
 */
class CoreDynamicContentView internal constructor() : CoreView() {

    val content = viewProperty<ICoreView?>(null) {
        it?.context?.terminate()
    }

    private val wrapperBuilder = TargetCoreViewBuilder(this, content)

    val transitionSpec = viewProperty<Any?>(null)

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

}

fun CoreViewBuilder.dynamicContentView(builder: CoreDynamicContentView.() -> Unit) {
    onNextView(CoreDynamicContentView(), builder)
}