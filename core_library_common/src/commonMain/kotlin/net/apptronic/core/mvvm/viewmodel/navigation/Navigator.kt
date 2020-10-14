package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent

abstract class Navigator<Content>(
        val parent: IViewModel
) : ViewModelParent, INavigator<Content> {

     val context: Context = parent.context

}