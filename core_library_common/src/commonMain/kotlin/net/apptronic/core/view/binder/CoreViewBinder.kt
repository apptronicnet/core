package net.apptronic.core.view.binder

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration

/**
 * Multiplatform binding container. Allows to build [CoreView] to be used by platform for creating platform
 * native layout.
 */
abstract class CoreViewBinder<T : ViewModel> : CoreViewBuilder {

    protected var config: ViewConfiguration? = null

    override val viewConfiguration: ViewConfiguration
        get() {
            return config!!
        }

    private var isRecycledState: Boolean = false

    private var view: CoreView? = null

    override val isRecycled: Boolean
        get() = isRecycledState

    override fun <T : CoreView> nextView(child: T, builder: T.() -> Unit) {
        if (isRecycledState) {
            child.recycle()
            return
        }
        child.apply(builder)
        val view = this.view
        if (view != null) {
            this.view = child
        } else {
            view?.recycle()
        }
    }

    abstract fun onBind(viewModel: T)

    override fun recycle() {
        view?.recycle()
        isRecycledState = true
    }

}