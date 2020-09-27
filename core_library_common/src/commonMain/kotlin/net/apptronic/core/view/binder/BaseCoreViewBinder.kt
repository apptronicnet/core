package net.apptronic.core.view.binder

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.widgets.textView

abstract class BaseCoreViewBinder<T : ViewModel> : CoreViewBuilder {

    private var config: ViewConfiguration? = null
    internal var viewModel: T? = null

    private var isRecycledState: Boolean = false

    private var view: CoreView? = null

    override val isRecycled: Boolean
        get() = isRecycledState

    override val viewConfiguration: ViewConfiguration
        get() {
            return config!!
        }

    override fun recycle() {
        view?.recycle()
        isRecycledState = true
    }

    fun setConfiguration(viewConfiguration: ViewConfiguration) {
        this.config = viewConfiguration
    }

    @Suppress("UNCHECKED_CAST")
    open fun setViewModel(viewModel: ViewModel) {
        this.viewModel = viewModel as T
    }

    open fun unbind() {

    }

    fun createCoreView(): CoreView {
        if (view == null) {
            textView {
                text(viewModel!!::class.simpleName ?: "ViewModel")
            }
        }
        return view!!
    }

    abstract fun performCreateCoreView()

    override fun <T : CoreView> nextView(child: T, builder: T.() -> Unit): T {
        if (isRecycledState) {
            child.recycle()
            return child
        }
        child.apply(builder)
        val view = this.view
        if (view != null) {
            this.view = child
        } else {
            view?.recycle()
        }
        return child
    }

}