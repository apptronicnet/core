package net.apptronic.core.view

import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.entity.Entity

/**
 * Creates container which wraps some dynamic view content
 */
class CoreDynamicContentView internal constructor() : CoreParentView() {

    val content = viewProperty<ICoreView?>(null) {
        it?.context?.terminate()
    }

    val transitionSpec = viewProperty<Any?>(null)

    fun <T> content(source: Entity<T>, builder: ICoreViewBuilder.(T) -> Unit) {
        source.subscribe(context) {
            StandaloneCoreViewBuilder(this).builder(it)
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

fun ICoreViewBuilder.dynamicContentView(builder: CoreDynamicContentView.() -> Unit): CoreDynamicContentView {
    return onNextView(CoreDynamicContentView(), builder)
}