package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModelParent

abstract class Navigator<Content>(
        val parent: IViewModel
) : ViewModelParent, INavigator<Content> {

     final override val parentContext: Context = parent.context

}