package net.apptronic.core.view.engine

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.utils.isInstanceOf
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ICoreViewRepresentable
import kotlin.reflect.KClass

@UnderDevelopment
abstract class RenderingEngine<RContext : RenderingContext<View>, View : Any> {

    abstract val renderingContext: RContext

    private inner class TypedAdapterFactory<CoreViewType : ICoreView>(
            val coreViewType: KClass<CoreViewType>,
            val factory: ViewAdapterFactory<RContext, CoreViewType, View, out View>
    ) {

        @Suppress("UNCHECKED_CAST")
        fun tryCreateAdapter(coreView: ICoreView): ViewAdapter<RContext, *, View, out View>? {
            if (coreView.isInstanceOf(coreViewType)) {
                return factory.createAdapter(renderingContext, coreView as CoreViewType)
            }
            return null
        }

    }

    private val typedFactoryList = mutableListOf<TypedAdapterFactory<*>>()

    protected fun <CoreViewType : ICoreView> registerFactory(
            coreViewType: KClass<CoreViewType>,
            factory: ViewAdapterFactory<RContext, CoreViewType, View, out View>
    ) {
        typedFactoryList.add(TypedAdapterFactory(coreViewType, factory))
    }

    protected inline fun <reified CoreViewType : ICoreView> registerFactory(
            factory: ViewAdapterFactory<RContext, CoreViewType, View, out View>
    ) {
        registerFactory(CoreViewType::class, factory)
    }

    internal fun createAdapter(coreView: ICoreView): ViewAdapter<RContext, *, View, out View> {
        for (factory in typedFactoryList) {
            val adapter = factory.tryCreateAdapter(coreView)
            if (adapter != null) {
                return adapter
            }
        }
        return fallbackAdapter(coreView)
    }

    abstract fun fallbackAdapter(coreView: ICoreView): ViewAdapter<RContext, *, View, out View>

    fun render(viewModel: IViewModel): ContextView<View> {
        TODO()
    }

    fun render(coreView: ICoreView): ContextView<View> {
        val adapter = createAdapter(coreView)
        adapter.renderingContextReference = renderingContext
        adapter.renderingEngineReference = this
        adapter.internalCreateView()
        adapter.renderView()
        return ContextView(adapter.view, adapter.context)
    }

    fun render(representable: ICoreViewRepresentable): ContextView<View> {
        return render(representable.view())
    }

}