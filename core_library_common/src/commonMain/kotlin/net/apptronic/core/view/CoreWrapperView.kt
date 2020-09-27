package net.apptronic.core.view

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration

/**
 * Creates container which wraps some dynamic view content
 */
class CoreWrapperView internal constructor(viewConfiguration: ViewConfiguration) : BaseCoreView(viewConfiguration) {

    private var themes = mutableListOf<CoreViewStyle>()

    private val wrapperBuilder = CoreWrapperBuilder()

    val wrappedView = viewProperty<CoreView?>(null)

    val transitionSpec = viewProperty<Any?>(null)

    private var subscription: Subscription? = null

    override fun theme(vararg theme: CoreViewStyle) {
        super.theme(*theme)
        themes.addAll(theme)
        wrappedView.doWithValue {
            it?.theme(*theme)
        }
    }

    private inner class CoreWrapperBuilder : CoreViewBuilder {

        override val viewConfiguration: ViewConfiguration
            get() = this@CoreWrapperView.viewConfiguration

        override val isRecycled: Boolean
            get() = this@CoreWrapperView.isRecycled

        override fun <T : CoreView> nextView(child: T, builder: T.() -> Unit): T {
            if (isRecycled) {
                child.recycle()
                return child
            }
            child.theme(*themes.toTypedArray())
            child.builder()
            recycleCurrentView()
            wrappedView.set(child)
            return child
        }

        override fun recycle() {
            this@CoreWrapperView.recycle()
        }

    }

    private var isRecycledState = false

    override val isRecycled: Boolean
        get() = isRecycledState

    private fun recycleCurrentView() {
        wrappedView.doWithValue {
            it?.recycle()
        }
    }

    fun <T> content(source: Observable<T>, builder: CoreViewBuilder.(T) -> Unit) {
        subscription?.unsubscribe()
        subscription = source.subscribe {
            wrapperBuilder.builder(it)
        }
    }

    override fun recycle() {
        subscription?.unsubscribe()
        recycleCurrentView()
        isRecycledState = true
    }

    fun transitionSpec(value: Any?) {
        transitionSpec.set(value)
    }

    fun transitionSpec(source: Observable<Any?>) {
        transitionSpec.set(source) {
            it
        }
    }

}

fun CoreViewBuilder.wrapperView(builder: CoreWrapperView.() -> Unit) {
    nextView(CoreWrapperView(viewConfiguration), builder)
}