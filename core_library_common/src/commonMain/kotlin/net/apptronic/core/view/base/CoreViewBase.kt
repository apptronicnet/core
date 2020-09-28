package net.apptronic.core.view.base

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.properties.LTR
import net.apptronic.core.view.properties.RTL

interface CoreViewBase : Contextual {

    override val context: CoreViewContext

    val ICoreView.isLTR: Boolean
        get() {
            return context.viewConfiguration.layoutDirection == LTR
        }

    val ICoreView.isRTL: Boolean
        get() {
            return context.viewConfiguration.layoutDirection == RTL
        }

    fun createChildCoreViewContext(): CoreViewContext = context.createChild()

}