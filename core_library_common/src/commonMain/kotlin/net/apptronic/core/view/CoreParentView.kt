package net.apptronic.core.view

abstract class CoreParentView : CoreView(), ICoreParentView {

    private val themesList = mutableListOf<CoreViewStyle>()

    override var themes: List<CoreViewStyle> = themesList

    final override fun onChildAdded(child: ICoreView) {
        (child as? CoreView)?.parent = this
        child.context.setParentContext(context)
        applyThemes(child)
    }

    final override fun applyThemes(target: ICoreView) {
        parent?.applyThemes(target)
        themes.forEach {
            it.applyTo(target)
        }
    }

    final override fun theme(vararg theme: CoreViewStyle) {
        themesList.addAll(theme)
        super<CoreView>.theme(*theme)
    }

}