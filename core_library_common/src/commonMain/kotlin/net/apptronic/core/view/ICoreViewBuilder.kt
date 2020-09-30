package net.apptronic.core.view

/**
 * Base class for objects which can create views
 */
interface ICoreViewBuilder {

    val viewBuilderParent: ICoreParentView?

    fun <T : ICoreView> onNextView(coreView: T, builder: T.() -> Unit = {}): T {
        viewBuilderParent?.onChildAdded(coreView)
        coreView.builder()
        return coreView
    }

}

class StandaloneCoreViewBuilder(override val viewBuilderParent: ICoreParentView?) : ICoreViewBuilder