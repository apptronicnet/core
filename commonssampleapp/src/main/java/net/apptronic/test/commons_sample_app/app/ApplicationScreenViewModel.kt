package net.apptronic.test.commons_sample_app.app

import net.apptronic.common.core.mvvm.viewmodel.ViewModel
import net.apptronic.common.core.mvvm.viewmodel.ViewModelContext

class ApplicationScreenViewModel(context: ViewModelContext) : ViewModel(context) {

    private val rootScreenViewModel = subModelContainer()

}