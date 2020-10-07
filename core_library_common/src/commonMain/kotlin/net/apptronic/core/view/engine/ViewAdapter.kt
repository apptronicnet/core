package net.apptronic.core.view.engine

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.terminate
import net.apptronic.core.view.CoreViewContext
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewPropertyConsumer

@UnderDevelopment
abstract class ViewAdapter<RContext : RenderingContext<View>, CoreView : ICoreView, View : Any, ViewType : View> : ViewPropertyConsumer {

    private var parentReference: ViewAdapter<RContext, *, View, out View>? = null
    internal lateinit var renderingEngineReference: RenderingEngine<RContext, View>
    internal lateinit var renderingContextReference: RContext
    internal lateinit var viewReference: ViewType

    internal fun internalCreateView() {
        viewReference = createView()
    }

    open val parent: ViewAdapter<RContext, *, View, out View>?
        get() {
            return parentReference
        }
    val renderingContext: RContext
        get() {
            return renderingContextReference
        }

    fun terminate() {
        context.terminate()
    }

    override val context = CoreViewContext()
    abstract val coreView: CoreView

    internal fun renderView() {
        viewReference = createView()
        renderView(view)
    }

    val view: ViewType
        get() {
            return viewReference
        }

    protected fun renderChild(coreView: ICoreView): ViewAdapter<RContext, *, View, out View> {
        val adapter = renderingEngineReference.createAdapter(coreView)
        adapter.renderingEngineReference = renderingEngineReference
        adapter.parentReference = this
        adapter.renderingContextReference = renderingContextReference
        adapter.internalCreateView()
        adapter.renderView()
        return adapter
    }

    protected fun renderUsing(view: ViewType, adapter: ViewAdapter<RContext, in CoreView, View, in ViewType>) {
        adapter.parentReference = parentReference
        adapter.renderingEngineReference = renderingEngineReference
        adapter.renderingContextReference = renderingContextReference
        adapter.viewReference = viewReference
        adapter.renderView(view)
    }

    protected open fun createView(): ViewType {
        throw UnsupportedOperationException("$this does not create view")
    }

    protected abstract fun renderView(view: ViewType)

}