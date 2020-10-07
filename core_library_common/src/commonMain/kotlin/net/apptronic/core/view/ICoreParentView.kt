package net.apptronic.core.view

import net.apptronic.core.UnderDevelopment

@UnderDevelopment
interface ICoreParentView : ICoreView {

    var themes: List<CoreViewStyle>

    fun onChildAdded(child: ICoreView) {
        (child as? CoreView)?.parent = this
        child.context.setParentContext(context)
        applyThemes(child)
    }

    fun applyThemes(target: ICoreView) {
        parent?.applyThemes(target)
        themes.forEach {
            it.applyTo(target)
        }
    }

    override fun theme(vararg theme: CoreViewStyle)

}