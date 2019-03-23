package net.apptronic.test.commons_sample_app.app

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.app.di.SomeInterface

class ApplicationScreenViewModel(context: ViewModelContext) : ViewModel(context) {

    private val rootScreenViewModel = subModelContainer()

    val someInterface = objects().get<SomeInterface>()

}