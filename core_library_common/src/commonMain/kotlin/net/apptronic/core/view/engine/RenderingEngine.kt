package net.apptronic.core.view.engine

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ICoreViewRepresentable

abstract class RenderingEngine<RenderingContext : IRenderingContext<View>, View> {

    abstract val renderingContext: RenderingContext

    abstract val adapters: List<IRenderAdapter<RenderingContext, *, out View>>

    fun render(viewModel: IViewModel): View {
        TODO()
    }

    fun render(coreView: ICoreView): View {
        TODO()
    }

    fun render(representable: ICoreViewRepresentable): View {
        return render(representable.view())
    }

}