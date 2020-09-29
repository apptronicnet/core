package net.apptronic.core.view

/**
 * Base class for objects which can create views
 */
interface CoreViewBuilder {

    fun <T : ICoreView> onNextView(coreView: T, builder: T.() -> Unit = {}): T {
        coreView.builder()
        return coreView
    }

}

open class LayerCoreViewBuilder(private val parent: ICoreView) : CoreViewBuilder {

    override fun <T : ICoreView> onNextView(coreView: T, builder: T.() -> Unit): T {
        parent.themes.forEach {
            it.applyTo(coreView)
        }
        return super.onNextView(coreView, builder)
    }

}

open class TargetCoreViewBuilder(
        parent: ICoreView,
        private val target: ViewProperty<ICoreView?>
) : LayerCoreViewBuilder(parent) {

    override fun <T : ICoreView> onNextView(coreView: T, builder: T.() -> Unit): T {
        return super.onNextView(coreView, builder).also {
            target.set(it)
        }
    }

}