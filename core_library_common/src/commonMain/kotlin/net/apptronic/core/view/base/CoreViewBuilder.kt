package net.apptronic.core.view.base

import net.apptronic.core.view.CoreView

interface CoreViewBuilder : CoreViewBase {

    fun nextView(child: CoreView)

}